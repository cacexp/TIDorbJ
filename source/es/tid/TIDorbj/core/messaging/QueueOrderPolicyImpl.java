/*
* MORFEO Project
* http://www.morfeo-project.org
*
* Component: TIDorbJ
* Programming Language: Java
*
* File: $Source$
* Version: $Revision: 349 $
* Date: $Date: 2009-01-07 09:56:41 +0100 (Wed, 07 Jan 2009) $
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
package es.tid.TIDorbj.core.messaging;

import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.BAD_POLICY_VALUE;
import org.omg.CORBA.Policy;
import org.omg.CORBA.PolicyError;
import org.omg.Messaging.OrderingHelper;
import org.omg.Messaging.QUEUE_ORDER_POLICY_TYPE;
import org.omg.Messaging.QueueOrderPolicy;
import org.omg.Messaging.QueueOrderPolicyHelper;
import org.omg.Messaging.QueueOrderPolicyLocalBase;
import org.omg.Messaging.ORDER_ANY;
import org.omg.Messaging.ORDER_TEMPORAL;
import org.omg.Messaging.ORDER_PRIORITY;
import org.omg.Messaging.ORDER_DEADLINE;

import es.tid.TIDorbj.core.cdr.CDRInputStream;
import es.tid.TIDorbj.core.cdr.CDROutputStream;

/**
 * @author caceres
 *
 */
public class QueueOrderPolicyImpl extends QueueOrderPolicyLocalBase
{

    short allowedOrders;
    
    
    
    /**
     * @param allowedOrders
     */
    public QueueOrderPolicyImpl(short allowedOrders)
    {
        super();
        this.allowedOrders = allowedOrders;
    }
    public short allowed_orders()
    {
        return allowedOrders;
    }
    
    public int policy_type()
    {        
        return QUEUE_ORDER_POLICY_TYPE.value;
    }
    
    public Policy copy()
    {
        return new QueueOrderPolicyImpl(this.allowedOrders);
    }

   
    public void destroy() {
    
    }
    
    public static QueueOrderPolicyImpl createPolicy(Any val)
	throws org.omg.CORBA.PolicyError
	{
	    try {
	        short policy_value = OrderingHelper.extract(val);
	
	        return new QueueOrderPolicyImpl(policy_value);
	    }
	    catch (BAD_PARAM bp) {
	        throw new PolicyError(BAD_POLICY_VALUE.value);
	    }
	}

    public static QueueOrderPolicyImpl read(CDRInputStream input)
    {
        short policy_value = input.read_short();
        
        if( (policy_value != ORDER_ANY.value) &&
            (policy_value != ORDER_TEMPORAL.value) && 
            (policy_value != ORDER_PRIORITY.value) &&
            (policy_value != ORDER_DEADLINE.value) ) {
            throw new MARSHAL();
        }
        
        return new QueueOrderPolicyImpl(policy_value);        
    }

    /**
     * @param output
     * @param queueOrderPolicy
     */
    public static void write(CDROutputStream output,
                             QueueOrderPolicy queueOrderPolicy)
    {
        output.write_ushort(queueOrderPolicy.allowed_orders());   
        
    }

}
