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
package es.tid.TIDorbj.core.typecode;

import org.omg.CORBA.BAD_TYPECODE;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

import es.tid.TIDorbj.core.cdr.CDRInputStream;

/**
 * The <code>ExceptionTypeCode</code> class represents a <code>TypeCode</code>
 * object which is associated with an IDL exception.
 * 
 * @autor Juan A. Ca&acute;ceres
 * @version 1.0
 */

public class ExceptionTypeCode extends StructTypeCode
{

    public ExceptionTypeCode()
    {
        m_kind = TCKind.tk_except;
    }

    public ExceptionTypeCode(String repositoryId, String name,
                             org.omg.CORBA.StructMember[] members)
    {
        super(repositoryId, name, members);
        m_kind = TCKind.tk_except;
    }

    /**
     * Skips the value asociated to the TypeCode. This operation is used by the
     * TIDorb's Any implementation an the subclass <code>skip_value()</code>
     * operations.
     * 
     * @param input
     *            must be alwais a reference to a CDRInputStream object.
     */

    public static boolean skip_value(TypeCode type, CDRInputStream input)
    {
        // skip the id
        input.skipString();
        // skip the members
        return StructTypeCode.skipValue(type, input);
    }

    /**
     * Copies and remarshals the given typecode value marshaled in an
     * InputStream to a <code>es.tid.TIDorbj.core.CDRInputStream</code>. This
     * method will alwais be invoked by this stream.
     * 
     * @param type
     *            the value <code>TypeCode</code>
     * @param input
     *            the <code>InputStream</code> where the value is marshaled
     * @param output
     *            the <code>es.tid.TIDorbj.core.CDRInputStream</code>
     * @pre the <code>TypeCode</code> must be a struct type
     */

    public static void remarshalValue(TypeCode type, InputStream input,
                                      OutputStream output)
    {
        // exception id
        output.write_string(input.read_string());
        StructTypeCode.remarshalValue(type, input, output);
    }

    /**
     * Compares two InputStream marshaled values of a given TypeCode to a
     * <code>es.tid.TIDorbj.core.CDRInputStream</code>. This method will
     * alwais be invoked by this stream.
     * 
     * @param type
     *            the value <code>TypeCode</code>
     * @param input_a
     *            the <code>InputStream</code> where one value is marshaled
     * @param input_b
     *            the <code>InputStream</code> where the value value is
     *            marshaled
     * @pre <code>type</code> must be a struct type.
     */

    public static boolean valuesEqual(org.omg.CORBA.TypeCode type,
                                      InputStream input_a, InputStream input_b)
    {
        if (input_a.read_string().equals(input_b.read_string()))
            return StructTypeCode.valuesEqual(type, input_a, input_b);
        else
            return false;

    }

    /**
     * Dumps the description of a given TypeCode.
     * 
     * @param type
     *            the <code>TypeCode</code>
     * @param output
     *            the output stream where the TypeCode will be dumped
     * @pre <code>type</code> must be a exception type.
     */
    public static void dump(TypeCode type, java.io.PrintWriter output)
        throws java.io.IOException
    {
        try {
            int member_length = type.member_count();

            output.print("[TYPECODE]{exception (");
            output.print(member_length);
            output.print(" members) ");
            ComplexTypeCode.dumpParams(type, output);
            output.print(" {");

            for (int i = 0; i < member_length; i++) {
                output.print(type.member_name(i));
                output.print(": ");
                TypeCodeDumper.dump(type.member_type(i), output);
            }
            output.print('}');
        }
        catch (BadKind bk) {
            throw new BAD_TYPECODE("Fault in exception operations:"
                                   + bk.toString());

        }
        catch (Bounds bds) {
            throw new BAD_TYPECODE("Fault in exception operations:"
                                   + bds.toString());
        }

    }

    /**
     * Dumps the description of a the marshaled value of a given TypeCode.
     * 
     * @param type
     *            the <code>TypeCode</code>
     * @param input
     *            the input stream where the value is marshaled
     * @param output
     *            the output stream where the value will be dumped
     * @pre the typecode must be an exception type
     * @return <code>true</code> if if has been possible dump the value.
     */

    public static boolean dumpValue(TypeCode type, InputStream input,
                                    java.io.PrintWriter output)
        throws java.io.IOException
    {
        try {
            int member_length = type.member_count();

            output.print("[VALUE]{exception ");
            output.print(type.name());
            output.print(": ");

            output.print("(exception id)");
            output.print(input.read_string());
            output.print(" - ");

            for (int i = 0; i < member_length; i++) {
                output.print('(');
                output.print(i);
                output.print(") ");
                output.print(type.member_name(i));
                output.print(": ");
                if (!TypeCodeDumper.dumpValue(type.member_type(i), input,
                                              output))
                    return false;
                output.print(" | ");
            }
            output.print(" END_STRUCT-}");

        }
        catch (BadKind bk) {
            throw new BAD_TYPECODE("Fault in exception operations:"
                                   + bk.toString());

        }
        catch (Bounds bds) {
            throw new BAD_TYPECODE("Fault in exception operations:"
                                   + bds.toString());
        }

        return true;
    }

}