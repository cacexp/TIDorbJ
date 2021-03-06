/*
* MORFEO Project
* http://www.morfeo-project.org
*
* Component: TIDorbJ
* Programming Language: Java
*
* File: $Source$
* Version: $Revision: 35 $
* Date: $Date: 2006-08-25 13:13:29 +0200 (Fri, 25 Aug 2006) $
* Last modified by: $Author: ldlfd $
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
package es.tid.TIDorbj.core.util;

/**
 * Manages the completion status of an operation.
 * <p>
 * Also, makes one thread to wait the completion.
 * <p>
 * The status are:
 * <ul>
 * <li><code>INITIAL</code>: the operation is not completed and there is not
 * any Thread waiting for the completion
 * <li>WAITING: there is a Thread that is waiting for the completion
 * <li>COMPLETED: the operation is completed.
 * </ul>
 * 
 * @autor Juan A. C&aacute;ceres
 * @version 1.0
 */

public class OperationCompletion
{

    public final static int INITIAL = 0;

    public final static int WAITING = 1;

    public final static int COMPLETED = 2;

    /**
     * The Completion status.
     */
    int m_state;

    public OperationCompletion()
    {
        m_state = INITIAL;
    }

    /**
     * @return whether or not the operation is completed
     */
    public synchronized boolean isCompleted()
    {
        return m_state == COMPLETED;
    }

    /**
     * The operation is completed.
     * <p>
     * If there is a thread waiting for the completion notifies it.
     */
    public synchronized void setCompleted()
    {
        int actual_state = m_state;

        m_state = COMPLETED;

        if (actual_state == WAITING)
            notify();
    }

    /**
     * Makes a thread wait for the operation completion (with a timeout).
     * 
     * @timeout the timeout
     */
    public synchronized void waitForCompletion(long timeout)
        throws java.lang.InterruptedException,
        es.tid.TIDorbj.core.util.OnlyOneThreadCanWait
    {
        if (m_state == INITIAL) {
            m_state = WAITING;
            wait(timeout);
        } else if (m_state == WAITING) {
            throw new es.tid.TIDorbj.core.util.OnlyOneThreadCanWait();
        }
        // else do nothing, is completed
    }
    
    /**
     * If there is a thread waiting for the completion notifies it.
     *
     */
    public synchronized void interruptWaiting()
    {
            notify();
    }

}