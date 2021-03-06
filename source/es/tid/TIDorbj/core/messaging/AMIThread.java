/*
* MORFEO Project
* http://www.morfeo-project.org
*
* Component: TIDorbJ
* Programming Language: Java
*
* File: $Source$
* Version: $Revision: 395 $
* Date: $Date: 2009-05-27 16:10:32 +0200 (Wed, 27 May 2009) $
* Last modified by: $Author: avega $
*
* (C) Copyright 2004 Telef�nica Investigaci�n y Desarrollo
*     S.A.Unipersonal (Telef�nica I+D)
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
package es.tid.TIDorbj.core.messaging;

import es.tid.TIDorbj.core.TIDORB;
import es.tid.TIDorbj.util.Trace;

/**
 * Execution thread. Gets request from the request queue and executes them.
 * 
 * @autor Javier Fdz. Mejuto
 * @version 1.0
 */
public class AMIThread extends Thread
{

    private AMIManager m_ami_manager = null;
    
    private AMILockList m_ami_lock_list = null;

    private boolean m_deactivated = false;

    private ThreadStateListener m_thread_state_listener = null;

    private String m_thread_name;

    private TIDORB m_orb;
    
    private Trace m_trace;

    

    /**
     * Constructor.
     * 
     * @param poaManager
     *            POAManager to which this ExecThread belongs.
     * @param number
     *            Id number of this ExecThread.
     */
    protected AMIThread(AMIManager amiManager)
    {
        m_ami_manager = amiManager;
        m_ami_lock_list = m_ami_manager.getAMILockList();
        m_orb = m_ami_manager.m_orb;
        m_trace = m_orb.m_trace;
    }

    /**
     * Set the ThreadStateListener. Any AMIThread should have a listener
     * (eventually the AMIThreadPool) before it is started.
     * 
     * @param l
     *            The listener.
     */
    protected void setThreadStateListener(ThreadStateListener l)
    {
        m_thread_state_listener = l;
    }

    /**
     * Checks the state of the POAManager. If the state is ACTIVE, then finish
     * returning true. If the state is HOLDING, then wait until state is
     * diferent from HOLDING. If the state is DISCARDING, then discard the
     * request and return false. If the state is INACTIVE, then reject the
     * request and return false.
     * 
     * @param request
     *            The request which is being processed.
     * @return Returns true if the request must be executed, otherwise returns
     *         false.
     */
    /*private boolean checkState( QueuedRequest request ) {

        boolean executeRequest = false;

        if (m_trace != null) {
            String[] msg = { toString(), " checking state of ",
                            m_ami_manager.toString() };
            m_trace.print(Trace.DEEP_DEBUG, msg);
        }

        synchronized (m_ami_manager.m_state_mutex) {
            boolean exit = false;
            while (!exit) {
                State state = m_poa_manager.get_state();
                if ((state == State.ACTIVE) && !request.getMustDiscard()) {
                    exit = true;
                    executeRequest = true;
                } else if (state == State.HOLDING) {
                    try {
                        // wait until state changes
                        m_poa_manager.m_state_mutex.wait();
                    } catch (InterruptedException ie) {}
                    exit = false;
                } else if (state == State.INACTIVE) {
                    // MISSING: define strategy for deactivated POAManagers
                    request.submitResponse( new org.omg.CORBA.TRANSIENT() );
                    exit = true;
                    executeRequest = false;
                } else if ( state == State.DISCARDING || 
                            request.getMustDiscard()  ){
                    //TODO: trace removed, restore it at each implementation
                    //TODO: change method signature to return possible errors
                    //in state check
                    request.submitResponse( new org.omg.CORBA.TRANSIENT() );
                    exit = true;
                    executeRequest = false;
                }
            }
        }
        return executeRequest;
    }*/

    /**
     * Execution loop.
     */
    public void run()
    {        
       
        while (!m_deactivated) {

            try {
        	
                if (m_trace != null) {
                    String[] msg = { toString(), ": sending AMI reply..." };
                    m_trace.print(Trace.DEEP_DEBUG, msg);
                }
                
                AMILock ami_lock = m_ami_lock_list.getFirstReady();
                
                if (ami_lock != null) {
                    // If there is a response ready, then go!!
                    m_thread_state_listener.setActive(this);
                    ami_lock.putReply();
                    m_thread_state_listener.setInactive(this);
                    
                } else if (m_thread_state_listener.threadCanDie(this)) {
                    // If restarted and no request, then commit suicide
                    m_deactivated = true;
                }

            }
            catch (Throwable unhandledException) {
                // Unhandled exception. Should never happen!!
                if (m_trace != null) {
                    m_trace.printStackTrace(Trace.DEBUG,
                                            toString() + "Unhanled Exception ",
                                            unhandledException);
                    m_trace.print(Trace.DEBUG, toString() + " dies...");
                }
                m_deactivated = true;
            }
        }
        m_thread_state_listener.threadHasDied(this);
    }

    /**
     * @return Returns the string representation of this Thread.
     */
    public synchronized String toString()
    {
        if (m_thread_name == null) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(super.toString());
            buffer.append(" in ");
            buffer.append(m_ami_manager.toString());
            m_thread_name = buffer.toString();
            buffer = null;
        }
        return m_thread_name;
    }

}
