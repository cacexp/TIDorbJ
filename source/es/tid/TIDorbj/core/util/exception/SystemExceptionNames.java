/*
* MORFEO Project
* http://www.morfeo-project.org
*
* Component: TIDorbJ
* Programming Language: Java
*
* File: $Source$
* Version: $Revision: 2 $
* Date: $Date: 2005-12-19 08:58:21 +0100 (Mon, 19 Dec 2005) $
* Last modified by: $Author: caceres $
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
package es.tid.TIDorbj.core.util.exception;

/**
 * <code>SystemException</code> names and Repository Ids.
 * 
 * @autor Juan A. C&aacute;ceres
 * @version 1.0
 */

public interface SystemExceptionNames
{

    String BAD_CONTEXT_id = "IDL:omg.org/CORBA/BAD_CONTEXT:1.0";

    String BAD_CONTEXT_name = "org.omg.CORBA.BAD_CONTEXT";

    String BAD_INV_ORDER_id = "IDL:omg.org/CORBA/BAD_INV_ORDER:1.0";

    String BAD_INV_ORDER_name = "org.omg.CORBA.BAD_INV_ORDER";

    String BAD_OPERATION_id = "IDL:omg.org/CORBA/BAD_OPERATION:1.0";

    String BAD_OPERATION_name = "org.omg.CORBA.BAD_OPERATION";

    String BAD_PARAM_id = "IDL:omg.org/CORBA/BAD_PARAM:1.0";

    String BAD_PARAM_name = "org.omg.CORBA.BAD_PARAM";

    String BAD_TYPECODE_id = "IDL:omg.org/CORBA/BAD_TYPECODE:1.0";

    String BAD_TYPECODE_name = "org.omg.CORBA.BAD_TYPECODE";

    String COMM_FAILURE_id = "IDL:omg.org/CORBA/COMM_FAILURE:1.0";

    String COMM_FAILURE_name = "org.omg.CORBA.COMM_FAILURE";

    String DATA_CONVERSION_id = "IDL:omg.org/CORBA/DATA_CONVERSION:1.0";

    String DATA_CONVERSION_name = "org.omg.CORBA.DATA_CONVERSION";

    String FREE_MEM_id = "IDL:omg.org/CORBA/FREE_MEM:1.0";

    String FREE_MEM_name = "org.omg.CORBA.FREE_MEM";

    String IMP_LIMIT_id = "IDL:omg.org/CORBA/IMP_LIMIT:1.0";

    String IMP_LIMIT_name = "org.omg.CORBA.IMP_LIMIT";

    String INITIALIZE_id = "IDL:omg.org/CORBA/INITIALIZE:1.0";

    String INITIALIZE_name = "org.omg.CORBA.INITIALIZE";

    String INTERNAL_id = "IDL:omg.org/CORBA/INTERNAL:1.0";

    String INTERNAL_name = "org.omg.CORBA.INTERNAL";

    String INTF_REPOS_id = "IDL:omg.org/CORBA/INTF_REPOS:1.0";

    String INTF_REPOS_name = "org.omg.CORBA.INTF_REPOS";

    String INV_FLAG_id = "IDL:omg.org/CORBA/INV_FLAG:1.0";

    String INV_FLAG_name = "org.omg.CORBA.INV_FLAG";

    String INV_IDENT_id = "IDL:omg.org/CORBA/INV_IDENT:1.0";

    String INV_IDENT_name = "org.omg.CORBA.INV_IDENT";

    String INV_OBJREF_id = "IDL:omg.org/CORBA/INV_OBJREF:1.0";

    String INV_OBJREF_name = "org.omg.CORBA.INV_OBJREF";

    String INV_POLICY_id = "IDL:omg.org/CORBA/INV_POLICY:1.0";

    String INV_POLICY_name = "org.omg.CORBA.INV_POLICY";

    String INVALID_TRANSACTION_id = "IDL:omg.org/CORBA/INVALID_TRANSACTION:1.0";

    String INVALID_TRANSACTION_name = "org.omg.CORBA.INVALID_TRANSACTION";

    String MARSHAL_id = "IDL:omg.org/CORBA/MARSHAL:1.0";

    String MARSHAL_name = "org.omg.CORBA.MARSHAL";

    String NO_IMPLEMENT_id = "IDL:omg.org/CORBA/NO_IMPLEMENT:1.0";

    String NO_IMPLEMENT_name = "org.omg.CORBA.NO_IMPLEMENT";

    String NO_MEMORY_id = "IDL:omg.org/CORBA/NO_MEMORY:1.0";

    String NO_MEMORY_name = "org.omg.CORBA.NO_MEMORY";

    String NO_PERMISSION_id = "IDL:omg.org/CORBA/NO_PERMISSION:1.0";

    String NO_PERMISSION_name = "org.omg.CORBA.NO_PERMISSION";

    String NO_RESOURCES_id = "IDL:omg.org/CORBA/NO_RESOURCES:1.0";

    String NO_RESOURCES_name = "org.omg.CORBA.NO_RESOURCES";

    String NO_RESPONSE_id = "IDL:omg.org/CORBA/NO_RESPONSE:1.0";

    String NO_RESPONSE_name = "org.omg.CORBA.NO_RESPONSE";

    String OBJECT_NOT_EXIST_id = "IDL:omg.org/CORBA/OBJECT_NOT_EXIST:1.0";

    String OBJECT_NOT_EXIST_name = "org.omg.CORBA.OBJECT_NOT_EXIST";

    String OBJ_ADAPTER_id = "IDL:omg.org/CORBA/OBJ_ADAPTER:1.0";

    String OBJ_ADAPTER_name = "org.omg.CORBA.OBJ_ADAPTER";

    String PERSIST_STORE_id = "IDL:omg.org/CORBA/PERSIST_STORE:1.0";

    String PERSIST_STORE_name = "org.omg.CORBA.PERSIST_STORE";
    
    String TIMEOUT_id = "IDL:omg.org/CORBA/TIMEOUT:1.0";
    
    String TIMEOUT_name = "org.omg.CORBA.TIMEOUT";

    String TRANSACTION_REQUIRED_id = "IDL:omg.org/CORBA/TRANSACTION_REQUIRED:1.0";

    String TRANSACTION_REQUIRED_name = "org.omg.CORBA.TRANSACTION_REQUIRED";

    String TRANSACTION_ROLLEDBACK_id = "IDL:omg.org/CORBA/TRANSACTION_ROLLEDBACK:1.0";

    String TRANSACTION_ROLLEDBACK_name = "org.omg.CORBA.TRANSACTION_ROLLEDBACK";

    String TRANSIENT_id = "IDL:omg.org/CORBA/TRANSIENT:1.0";

    String TRANSIENT_name = "org.omg.CORBA.TRANSIENT";

    String UNKNOWN_id = "IDL:omg.org/CORBA/UNKNOWN:1.0";

    String UNKNOWN_name = "org.omg.CORBA.UNKNOWN";
}