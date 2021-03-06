/*
* MORFEO Project
* http://www.morfeo-project.org
*
* Component: TIDorbJ
* Programming Language: Java
*
* File: $Source$
* Version: $Revision: 307 $
* Date: $Date: 2008-10-28 11:06:44 +0100 (Tue, 28 Oct 2008) $
* Last modified by: $Author: avega $
*
* (C) Copyright 2004 Telefónica Investigación y Desarrollo
*     S.A.Unipersonal (Telefónica I+D)
*
* Info about members and contributors of the MORFEO project
* is available at:
*
*   http://www.morfeo-project.org/TIDorbJ/CREDITS
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*
* If you want to use this software an plan to distribute a
* proprietary application in any way, and you are not licensing and
* distributing your source code under GPL, you probably need to
* purchase a commercial license of the product.  More info about
* licensing options is available at:
*
*   http://www.morfeo-project.org/TIDorbJ/Licensing
*/    
package es.tid.TIDorbj.core.poa;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;


import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.TRANSIENT;

import es.tid.TIDorbj.util.Trace;

/**
 * Queue of requests to be read by the execution thread. Implements QoS Messaging
 * QueueOrderPolicy.
 * 
 * @autor Javier Fdz. Mejuto
 * @autor Juan A. Caceres
 * @version 2.0
 */
class RequestQueue {

    // Naive implementation
    private TreeSet m_values = null;

    private QueueReaderManager m_queue_read_manager = null;

    private POAManagerImpl m_poa_manager;
    
    private boolean m_deactivation;

    /**
     * Constructor.
     * 
     * @param poaManager
     *            POAManager to which this RequestQueue belongs.
     * @param reader
     *            Object that manages the creation of new readers for this
     *            queue.
     */
    public RequestQueue(POAManagerImpl poaManager,                        
                        QueueReaderManager reader,
                        Comparator comparator) {
        m_values = new TreeSet(comparator);        
        m_queue_read_manager = reader;
        m_poa_manager = poaManager;        
        m_deactivation = false;
    }
    
    public synchronized void setComparator(Comparator comparator)
    {
        TreeSet aux = m_values;        
        m_values = new TreeSet(comparator);
        m_values.addAll(aux);        
    }

    /**
     * Adds an new request to the queue.
     * 
     * @param request The request to be added.
     */
    synchronized public void add(QueuedRequest request) {
        if ( m_deactivation  || 
             m_values.size() >= m_poa_manager.m_conf.getMaxQueuedRequests()) {
            TRANSIENT e = new TRANSIENT(null, 1, CompletionStatus.COMPLETED_NO);
            if ( m_poa_manager.m_orb.m_trace != null ) {
                String[] msg = { toString(),
                                 " Submitting TRANSIENT in request because",
                                 " MaxQueuedRequests ",
                                 Integer.toString(m_poa_manager.m_conf.getMaxQueuedRequests()),
                                 " has been reached" };
                m_poa_manager.m_orb.printTrace(Trace.DEEP_DEBUG, msg);
            }
            request.submitResponse( e );
        } else {
            m_values.add(request);
            if (!m_queue_read_manager.createNewReader()) {
                notify();
            }
        }
    }

    /**
     * Gets (and removes) the first element of the queue.
     * 
     * @return The first element of the queue.
     */
    public synchronized QueuedRequest get() {
        if ( m_values.isEmpty() ) {
            if (m_deactivation) {
                return null;
            } else {
                try {
                    wait(m_poa_manager.m_conf.getStarvingTime());
                } catch (InterruptedException ie) {}
                if ( m_values.isEmpty() ) {
                    return null;
                }
            }
        }
        //first element out
        QueuedRequest req = (QueuedRequest) m_values.first();
        
        m_values.remove(req);         
        
        return req;
    }

    /**
     * @return Number of enqueued elements.
     */
    synchronized public int size() {
        return m_values.size();
    }

    /**
     * Set all request to "discarding".
     */
    synchronized public void discardAll() {
        
        Iterator it  = m_values.iterator();
        while (it.hasNext()){
            QueuedRequest req = (QueuedRequest) it.next();
            req.setMustDiscard(true);
        }
    }

    /**
     * The POAManager is being deactivating, notify it to all blocked threads.
     */

    synchronized void deactivation() {
        if (!m_deactivation) {
            m_deactivation = true;
            notifyAll();
        }
    }

}
