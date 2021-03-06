/*
* MORFEO Project
* http://www.morfeo-project.org
*
* Component: TIDorbJ
* Programming Language: Java
*
* File: $Source$
* Version: $Revision: 478 $
* Date: $Date: 2011-04-29 16:42:47 +0200 (Fri, 29 Apr 2011) $
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
package es.tid.TIDorbj.core.comm.iiop;

import es.tid.TIDorbj.core.comm.giop.RequestId;

public class LockList
{

    java.util.Hashtable m_table;

    public LockList()
    {
        m_table = new java.util.Hashtable();
    }

    public void put(Lock lock)
    {
        m_table.put(lock.requestId(), lock);
    }

    public Lock get(RequestId request_id)
    {
        return (Lock) m_table.get(request_id);
    }

    public void remove(RequestId request_id)
    {
        m_table.remove(request_id);
    }

    public int size()
    {
        return m_table.size();
    }

    public LockEnumeration elements()
    {
        return new LockEnumeration(m_table.elements());
    }

}
