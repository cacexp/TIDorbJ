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
package es.tid.TIDorbj.core;

import org.omg.CORBA.BAD_PARAM;

import es.tid.TIDorbj.core.typecode.AliasTypeCode;
import es.tid.TIDorbj.core.typecode.ArrayTypeCode;
import es.tid.TIDorbj.core.typecode.EnumTypeCode;
import es.tid.TIDorbj.core.typecode.ExceptionTypeCode;
import es.tid.TIDorbj.core.typecode.FixedTypeCode;
import es.tid.TIDorbj.core.typecode.NativeTypeCode;
import es.tid.TIDorbj.core.typecode.ObjectRefTypeCode;
import es.tid.TIDorbj.core.typecode.RecursiveTypeCode;
import es.tid.TIDorbj.core.typecode.SequenceTypeCode;
import es.tid.TIDorbj.core.typecode.StringTypeCode;
import es.tid.TIDorbj.core.typecode.StructTypeCode;
import es.tid.TIDorbj.core.typecode.TypeCodeFactory;
import es.tid.TIDorbj.core.typecode.UnionTypeCode;
import es.tid.TIDorbj.core.typecode.ValueBoxTypeCode;
import es.tid.TIDorbj.core.typecode.ValueTypeCode;
import es.tid.TIDorbj.core.typecode.WStringTypeCode;

/**
 * TIDorb Version 2.3 CORBA ORB Singleton.
 * <p>
 * @autor Juan A. C&aacute;ceres
 * @version 1.0
 */

public class SingletonORB extends org.omg.CORBA_2_3.ORB
{

    /**
     * ORB configuration.
     */

    public ConfORB m_conf;

    /**
     * ORB singleton static reference: only a instance will be for all orb.
     * This rerefence will be returned by <code>init()</code> method.
     */

    protected static SingletonORB st_orb = null;

    public SingletonORB()
    {
        m_conf = new ConfORB();
    }

    public static org.omg.CORBA.ORB init()
    {
        if (st_orb == null)
            st_orb = new SingletonORB();

        return st_orb;
    }

    public static org.omg.CORBA.ORB init(String[] args,
                                         java.util.Properties props)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public static org.omg.CORBA.ORB init(java.applet.Applet app,
                                         java.util.Properties props)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    protected void set_parameters(String[] args, java.util.Properties props)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    protected void set_parameters(java.applet.Applet app,
                                  java.util.Properties props)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public void connect(org.omg.CORBA.Object obj)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public void disconnect(org.omg.CORBA.Object obj)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public String[] list_initial_services()
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.Object resolve_initial_references(String object_name)
        throws org.omg.CORBA.ORBPackage.InvalidName
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public String object_to_string(org.omg.CORBA.Object obj)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.Object string_to_object(String str)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.NVList create_list(int count)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.NVList create_operation_list(org.omg.CORBA.Object oper)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.NamedValue create_named_value(String s,
                                                       org.omg.CORBA.Any any,
                                                       int flags)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.ExceptionList create_exception_list()
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.ContextList create_context_list()
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.Context get_default_context()
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.Environment create_environment()
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.portable.OutputStream create_output_stream()
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public void send_multiple_requests_oneway(org.omg.CORBA.Request[] req)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public void send_multiple_requests_deferred(org.omg.CORBA.Request[] req)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public boolean poll_next_response()
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.Request get_next_response()
        throws org.omg.CORBA.WrongTransaction
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.TypeCode get_primitive_tc(org.omg.CORBA.TCKind tcKind)
    {
        if (tcKind == null)
            throw new BAD_PARAM("Null TCKind reference");

        return TypeCodeFactory.getBasicTypeCode(tcKind);
    }

    public org.omg.CORBA.TypeCode 
    	create_struct_tc( String id,
    	                  String name,
    	                  org.omg.CORBA.StructMember[] members)
    {
        if ((id == null) || (name == null) || (members == null))
            throw new BAD_PARAM("Null reference");

        return new StructTypeCode(id, name, members);
    }

    public org.omg.CORBA.TypeCode 
    	create_union_tc(String id,
    	                String name,
    	                org.omg.CORBA.TypeCode discriminator_type,
    	                org.omg.CORBA.UnionMember[] members)
    {
        if ((id == null) || (name == null) || (discriminator_type == null)
            || (members == null))
            throw new BAD_PARAM("Null reference");

        return new UnionTypeCode(id, name, discriminator_type, members);
    }

    public org.omg.CORBA.TypeCode create_enum_tc(String id, String name,
                                                 String[] members)
    {
        if ((id == null) || (name == null) || (members == null))
            throw new BAD_PARAM("Null reference");

        return new EnumTypeCode(id, name, members);
    }

    public org.omg.CORBA.TypeCode
    	create_alias_tc(String id,
    	                String name,
    	                org.omg.CORBA.TypeCode original_type)
    {
        if ((id == null) || (name == null) || (original_type == null))
            throw new BAD_PARAM("Null reference");

        return new AliasTypeCode(id, name, original_type);
    }

    public org.omg.CORBA.TypeCode 
    	create_exception_tc(java.lang.String id,
    	                    java.lang.String name,
    	                    org.omg.CORBA.StructMember[] members)
    {
        if ((id == null) || (name == null) || (members == null))
            throw new BAD_PARAM("Null reference");

        return new ExceptionTypeCode(id, name, members);
    }

    public org.omg.CORBA.TypeCode create_string_tc(int bound)
    {
        if (bound < 0)
            throw new BAD_PARAM("Illegal string length " + bound);

        return new StringTypeCode(bound);
    }

    public org.omg.CORBA.TypeCode create_wstring_tc(int bound)
    {
        if (bound < 0)
            throw new BAD_PARAM("Illegal string length " + bound);

        return new WStringTypeCode(bound);
    }

    public org.omg.CORBA.TypeCode 
    	create_sequence_tc(int bound,
    	                   org.omg.CORBA.TypeCode element_type)
    {
        if (bound < 0)
            throw new BAD_PARAM("Illegal sequence length " + bound);

        if (element_type == null)
            throw new BAD_PARAM("Null TypeCode reference");

        return new SequenceTypeCode(element_type, bound);
    }

    /**
     * @deprecated
     */
    public org.omg.CORBA.TypeCode 
    	create_recursive_sequence_tc(int bound,int offset)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.TypeCode 
    	create_array_tc(int length, org.omg.CORBA.TypeCode element_type)
    {
        if (length < 0)
            throw new BAD_PARAM("Illegal array length " + length);

        if (element_type == null)
            throw new BAD_PARAM("Null TypeCode reference");

        return new ArrayTypeCode(element_type, length);
    }

    public org.omg.CORBA.TypeCode create_native_tc(String id, String name)
    {
        if ((id == null) || (name == null))
            throw new BAD_PARAM("Null reference");

        return new NativeTypeCode(id, name);
    }

    public org.omg.CORBA.TypeCode create_interface_tc(String id, String name)
    {
        if ((id == null) || (name == null))
            throw new BAD_PARAM("Null reference");

        return new ObjectRefTypeCode(id, name);
    }

    public org.omg.CORBA.TypeCode create_fixed_tc(short digits, short scale)
    {
        return new FixedTypeCode(digits, scale);
    }

    public org.omg.CORBA.TypeCode 
    	create_value_tc( String id,
    	                 String name,
    	                 short type_modifier,
    	                 org.omg.CORBA.TypeCode concrete_base,
    	                 org.omg.CORBA.ValueMember[] members)
    {
        if ((id == null) || (name == null) || (members == null))
            throw new BAD_PARAM("Null reference");

        return new ValueTypeCode(id, name, type_modifier, concrete_base,
                                 members);
    }

    public org.omg.CORBA.TypeCode create_recursive_tc(String id)
    {
        if (id == null)
            throw new BAD_PARAM("Null String reference");

        return new RecursiveTypeCode(id);
    }

    public org.omg.CORBA.TypeCode 
    	create_value_box_tc( String id,
    	                     String name,
    	                     org.omg.CORBA.TypeCode boxed_type)
    {
        if ((id == null) || (name == null) || (boxed_type == null))
            throw new BAD_PARAM("Null reference");

        return new ValueBoxTypeCode(id, name, boxed_type);
    }

    public org.omg.CORBA.Any create_any()
    {
        return new es.tid.TIDorbj.core.AnyImpl(null);
    }

    // corba 2.3 operations

    // always return a ValueDef or throw BAD_PARAM if not repid of a value
    public org.omg.CORBA.Object get_value_def(String repid)
        throws org.omg.CORBA.BAD_PARAM
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.portable.ValueFactory 
    	register_value_factory( String id,
    	                        org.omg.CORBA.portable.ValueFactory factory)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public void unregister_value_factory(String id)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public org.omg.CORBA.portable.ValueFactory lookup_value_factory(String id)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public void set_delegate(java.lang.Object wrapper)
    {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

}