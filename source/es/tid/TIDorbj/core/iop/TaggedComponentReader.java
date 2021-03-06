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
package es.tid.TIDorbj.core.iop;

import org.omg.IOP.TAG_ORB_TYPE;
import org.omg.IOP.TAG_POLICIES;
import org.omg.IOP.TAG_SSL_SEC_TRANS;
import org.omg.IOP.TAG_CSI_SEC_MECH_LIST;

import es.tid.TIDorbj.core.messaging.PoliciesComponent;
import es.tid.TIDorbj.core.comm.ssliop.SSLComponent;
import es.tid.TIDorbj.core.security.CSIComponent;

/**
 * Helper class for read TaggedComponent structures.
 * 
 * @autor Juan A. C&aacute;ceres
 * @version 1.0
 */
public abstract class TaggedComponentReader
{

    public static TaggedComponent 
    	read(es.tid.TIDorbj.core.cdr.CDRInputStream input)
    {
        int profile_id = input.read_ulong();

        switch (profile_id)
        {
            case TAG_ORB_TYPE.value:
                ORBComponent orb_component = new ORBComponent();
                orb_component.partialRead(input);
                return orb_component;
            case TAG_POLICIES.value:
            	PoliciesComponent policies = new PoliciesComponent();
            	policies.partialRead(input);
            	return policies;
            case TAG_SSL_SEC_TRANS.value:
            	SSLComponent ssl_component = new SSLComponent();
            	ssl_component.partialRead(input);
            	return ssl_component;
            case TAG_CSI_SEC_MECH_LIST.value:
            	CSIComponent csi_component = new CSIComponent();
            	csi_component.partialRead(input);
            	return csi_component;
            default:
                TaggedComponent comp = new TaggedComponent(profile_id);
                comp.partialRead(input);
                return comp;
        }
    }
}
