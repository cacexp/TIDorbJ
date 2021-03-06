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
package es.tid.TIDorbj.core;

import org.omg.CORBA.ARG_IN;
import org.omg.CORBA.ARG_INOUT;
import org.omg.CORBA.ARG_OUT;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.Bounds;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Object;
import org.omg.CORBA.Request;
import org.omg.CORBA.UnknownUserException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.Messaging.ReplyHandler;
import org.omg.Messaging.ReplyHandlerHelper;
import org.omg.Messaging.ReplyHandlerOperations;
import org.omg.Messaging.ReplyHandlerPOA;
import org.omg.Messaging._ReplyHandlerStub;

import es.tid.TIDorbj.core.comm.CommunicationDelegate;
import es.tid.TIDorbj.core.comm.giop.RequestId;
import es.tid.TIDorbj.core.policy.PolicyContext;
import es.tid.TIDorbj.util.Trace;

/**
 * TIDorb DII Request implementation.
 * 
 * @autor Juan A. C&aacute;ceres
 * @version 1.0
 */

public class RequestImpl extends org.omg.CORBA.Request
{
    /**
     * The orb.
     */
    TIDORB m_orb;

    /**
     * Target CORBA object.
     */
    org.omg.CORBA.portable.ObjectImpl m_target;

    /**
     * operation name.
     */
    String m_operation_name;

    /**
     * Request identifier.
     */
    RequestId m_request_id;

    /**
     * Needs response.
     */
    boolean m_with_response = false;

    /**
     * Reliable oneway request that needs response.
     */
    boolean m_reliable_oneway = false;

    /**
     * Completion status needed in exception throwing. Initialy, the status is
     * <code>COMPLETED_NO</code>
     */
    CompletionStatus m_completed;

    /**
     * Request context
     */
    org.omg.CORBA.Context m_context;

    /**
     * Request arguments
     */
    org.omg.CORBA.NVList m_arguments;

    /**
     * Request result value
     */
    org.omg.CORBA.NamedValue m_result;

    /**
     * Request exception list
     */
    org.omg.CORBA.ExceptionList m_exceptions;

    /**
     * Request context list
     */
    org.omg.CORBA.ContextList m_contextlist;

    /**
     * Request environment
     */
    EnvironmentImpl m_environment;

    /**
     * Request Policy Context
     */
    PolicyContext m_policy_context;

    /**
     * ReplyHandler object reference for AMI callback model
     */
    Object m_handler;


    public RequestImpl(org.omg.CORBA.Object target, org.omg.CORBA.Context ctx,
                       String operation, org.omg.CORBA.NVList arg_list,
                       org.omg.CORBA.NamedValue result,
                       org.omg.CORBA.ExceptionList exclist,
                       org.omg.CORBA.ContextList ctxlist)
    {
        m_completed = CompletionStatus.COMPLETED_NO;

        m_target = (org.omg.CORBA.portable.ObjectImpl) target;

        org.omg.CORBA.ORB obj_orb = m_target._orb();

        if ((obj_orb != null) && (obj_orb instanceof TIDORB))
            m_orb = (TIDORB) obj_orb;
        else
            throw new BAD_PARAM("Invalid ORB");

        m_request_id = null;

        m_operation_name = operation;
        m_context = ctx;
        m_arguments = arg_list;
        m_result = result;
        m_exceptions = exclist;
        m_contextlist = ctxlist;
        m_environment = new EnvironmentImpl();
        m_policy_context = null;
        m_handler = null;
    }

    /**
     * Set the completion status to COMPLETED_MAYBE
     */
    public void setCompletedMaybe()
    {
        m_completed = CompletionStatus.COMPLETED_MAYBE;
    }

    /**
     * Set the completion status to COMPLETED_YES
     */
    public void setCompletedYes()
    {
        m_completed = CompletionStatus.COMPLETED_YES;
    }

    /**
     * @return the current completion status
     */
    public CompletionStatus getCompleted()
    {
        return m_completed;
    }

    public org.omg.CORBA.Object target()
    {
        return m_target;
    }

    public String operation()
    {
        return m_operation_name;
    }

    public org.omg.CORBA.NVList arguments()
    {
        return m_arguments;
    }

    public org.omg.CORBA.NamedValue result()
    {
        return m_result;
    }

    public org.omg.CORBA.Environment env()
    {
        return m_environment;
    }

    public void setUserException(Any exc)
    {
        m_environment.exception(new UnknownUserException(exc));
    }

    public void setSystemException(org.omg.CORBA.SystemException exc)
    {
        m_environment.exception(exc);
    }

    public org.omg.CORBA.ExceptionList exceptions()
    {
        return m_exceptions;
    }

    public org.omg.CORBA.ContextList contexts()
    {
        return m_contextlist;
    }

    public void ctx(org.omg.CORBA.Context ctx)
    {
        if (ctx == null)
            throw new BAD_PARAM("Null Context reference", 0,
                                CompletionStatus.COMPLETED_NO);

        m_context = ctx;
    }

    public org.omg.CORBA.Context ctx()
    {
        return m_context;
    }

    public org.omg.CORBA.Any add_in_arg()
    {
        return addArg(ARG_IN.value);
    }

    public org.omg.CORBA.Any add_named_in_arg(String name)
    {
        if (name == null)
            throw new BAD_PARAM("Null string reference", 0,
                                CompletionStatus.COMPLETED_NO);

        return addArg(name, ARG_IN.value);
    }

    public org.omg.CORBA.Any add_inout_arg()
    {
        return addArg(ARG_INOUT.value);
    }

    public org.omg.CORBA.Any add_named_inout_arg(String name)
    {
        if (name == null)
            throw new BAD_PARAM("Null string reference", 0,
                                CompletionStatus.COMPLETED_NO);

        return addArg(name, ARG_INOUT.value);
    }

    public org.omg.CORBA.Any add_out_arg()
    {
        return addArg(ARG_OUT.value);
    }

    public org.omg.CORBA.Any add_named_out_arg(String name)
    {
        if (name == null)
            throw new BAD_PARAM("Null string reference", 0,
                                CompletionStatus.COMPLETED_NO);

        return addArg(name, ARG_OUT.value);
    }

    public void set_return_type(org.omg.CORBA.TypeCode tc)
    {
        if (tc == null)
            throw new BAD_PARAM("Null TypeCode reference", 0,
                                CompletionStatus.COMPLETED_NO);

        Any result_any = m_orb.create_any();
        result_any.type(tc);
        m_result = NamedValueImpl.from_int(ARG_OUT.value, "", result_any);
    }

    public org.omg.CORBA.Any return_value()
    {
        if (m_result != null)
            return m_result.value();
        else
            throw new BAD_OPERATION("No result defined.");
    }

    public void readResult(InputStream input)
    {
        if (m_result != null)
            m_result.value().read_value(input, m_result.value().type());
    }

    public void invoke()
    {
        try {
            m_with_response = true;

            CommunicationDelegate delegate = (CommunicationDelegate) m_target._get_delegate();

            delegate.invoke(this);

        }
        catch (org.omg.CORBA.SystemException se) {
            setSystemException(se);
            if (m_orb.m_trace != null) {
                m_orb.m_trace.printStackTrace(Trace.DEEP_DEBUG,
                                              "RequestImpl.invoke():" +
                                              " system exception",
                                              se);
            }
        }
        catch (Throwable th) {
            org.omg.CORBA.SystemException se = new org.omg.CORBA.UNKNOWN();
            setSystemException(se);
            if (m_orb.m_trace != null) {
                m_orb.m_trace.printStackTrace(Trace.ERROR,
                                              "RequestImpl.invoke():" +
                                              " Unexpected exception",
                                              th);
            }
        }

        Exception e = m_environment.exception();

        if ((e != null) && (e instanceof org.omg.CORBA.SystemException))
            throw (org.omg.CORBA.SystemException) e;
    }

    public void send_oneway()
    {

        try {
            m_with_response = false;

            CommunicationDelegate delegate = (CommunicationDelegate) m_target._get_delegate();

            delegate.onewayRequest(this);

        }
        catch (org.omg.CORBA.SystemException se) {
            se.completed = m_completed;
            throw se;
        }

    }
    
	public void send_deferred () {
        throw new org.omg.CORBA.NO_IMPLEMENT();
		
		
		/*try {
			m_handler = m_arguments.item(0).value().extract_Object();
			m_arguments.remove(0);
		} catch (BAD_OPERATION e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Bounds e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	m_with_response = true;
        CommunicationDelegate delegate = (CommunicationDelegate) m_target._get_delegate();
        delegate.asyncRequest(this);*/
	}

    public void get_response()
        throws org.omg.CORBA.WrongTransaction
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public boolean poll_response()
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

// BUG [#956] Invalid call to org.omg.CORBA.Request.sendc(org.omg.CORBA.Object) using AMI
//     //Additional Messaging Operations
// 	public void sendc(org.omg.CORBA.Object handler) {
// 		m_handler = handler;

// 		try {
//         	m_with_response = true;
//             CommunicationDelegate delegate = (CommunicationDelegate) m_target._get_delegate();
//             //delegate.asyncRequest(this);
//             delegate.asyncRequest(this, m_handler);
//         }
//         catch (org.omg.CORBA.SystemException se) {
//             setSystemException(se);
//             if (m_orb.m_trace != null) {
//                 m_orb.m_trace.printStackTrace(Trace.DEEP_DEBUG,
//                                               "RequestImpl.send_deferred():" +
//                                               " system exception",
//                                               se);
//             }
//         }
//         catch (Throwable th) {
//             org.omg.CORBA.SystemException se = new org.omg.CORBA.UNKNOWN();
//             setSystemException(se);
//             if (m_orb.m_trace != null) {
//                 m_orb.m_trace.printStackTrace(Trace.ERROR,
//                                               "RequestImpl.send_deferred():" +
//                                               " Unexpected exception",
//                                               th);
//             }
//         }

//         Exception e = m_environment.exception();

//         if ((e != null) && (e instanceof org.omg.CORBA.SystemException))
//             throw (org.omg.CORBA.SystemException) e;
//     }
    
	public Object sendp() {
        throw new org.omg.CORBA.NO_IMPLEMENT();
	}

	public void prepare(Object p) {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }
    
    // TIDOrb operations

    public TIDORB orb()
    {
        return m_orb;
    }

    public RequestId getId()
    {
        return m_request_id;
    }

    public void setId(RequestId id)
    {
        m_request_id = id;
    }

    public void reliableOneway(boolean value)
    {
        m_reliable_oneway = value;
    }

    public boolean reliableOneway()
    {
        return m_reliable_oneway;
    }

    public void withResponse(boolean value)
    {
        m_with_response = value;
    }

    public boolean withResponse()
    {
        return m_with_response;
    }

    private org.omg.CORBA.Any addArg(String name, int flag_value)
    {
        if (m_arguments == null)
            m_arguments = new NVListImpl(m_orb);

        NamedValueImpl arg;
        arg = (NamedValueImpl) m_arguments.add_item(name, flag_value);
        return arg.value();
    }

    private org.omg.CORBA.Any addArg(int flag_value)
    {
        if (m_arguments == null)
            m_arguments = new NVListImpl(m_orb);

        NamedValueImpl arg;
        arg = (NamedValueImpl) m_arguments.add(flag_value);
        return arg.value();
    }

    public void writeInParams(es.tid.TIDorbj.core.cdr.CDROutputStream output)
    {
        if (m_arguments != null)
            NVListImpl.writeInParams(m_arguments, output);
    }

    public void setPolicyContext(PolicyContext context)
    {
        m_policy_context = context;        
    }
    
    public PolicyContext getPolicyContext()
    {
        if (m_policy_context == null) {

        	CommunicationDelegate delegate =
                (CommunicationDelegate) m_target._get_delegate();      	
            
            
            m_policy_context = delegate.createRequestPolicyContext();
        }

        return m_policy_context;
    }

    public void set_ami_handler(Object handler)
    {
    	m_handler = handler;
    }

    public Object get_ami_handler()
    {
    	return m_handler;
    }

    public void setOperationName (String operName) {
    	m_operation_name = operName;
    }
}
