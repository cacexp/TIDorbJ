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
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

import es.tid.TIDorbj.core.cdr.CDRInputStream;
import es.tid.TIDorbj.core.cdr.CDROutputStream;

/**
 * The <code>FixedTypeCode</code> class represents a <code>TypeCode</code>
 * object which is associated with an IDL fixed.
 * 
 * @autor Juan A. Ca&acute;ceres
 * @version 1.0
 */

public class FixedTypeCode extends TypeCodeImpl
{

    protected short m_digits;

    protected short m_scale;

    public FixedTypeCode()
    {
        super(TCKind.tk_fixed);
    }

    public FixedTypeCode(short digits, short scale)
    {
        super(TCKind.tk_fixed);
        this.m_digits = digits;
        this.m_scale = scale;
    }

    public boolean equal(org.omg.CORBA.TypeCode tc)
    {
        try {
            return super.equal(tc) && (m_digits == tc.fixed_digits())
                   && (m_scale == tc.fixed_scale());
        }
        catch (org.omg.CORBA.TypeCodePackage.BadKind e) {
            return false;
        }
    }

    public short fixed_digits()
        throws org.omg.CORBA.TypeCodePackage.BadKind
    {
        return m_digits;
    }

    public short fixed_scale()
        throws org.omg.CORBA.TypeCodePackage.BadKind
    {
        return m_scale;
    }

    //TIDORB operations

    public static int valueLength(TypeCode type)
    {
        try {
            int fix_digits = type.fixed_digits();

            return (fix_digits + (fix_digits % 2)) / 2; //it must have a odd
            // number of octets
        }
        catch (BadKind bk) {
            throw new BAD_TYPECODE("Fault in Fixed operation fixed_digits().");
        }
    }

    public boolean isSimple()
    {
        return false;
    }

    public static void skipParams(es.tid.TIDorbj.core.cdr.CDRInputStream input)
    {
        input.skipUshort();
        input.skipShort();
    }

    public void partialUnmarshal(es.tid.TIDorbj.core.cdr.CDRInputStream input)
    {
        m_digits = input.read_ushort();
        m_scale = input.read_short();
    }

    /**
     * Skips the value asociated to the TypeCode. This operation is used by the
     * TIDorb's Any implementation an the subclass <code>skip_value()</code>
     * operations.
     * 
     * @param input
     *            must be alwais a reference to a CDRInputStream object.
     */

    public static boolean skipValue(TypeCode type, CDRInputStream input)
    {
        int length = valueLength(type);
        input.skipOctetArray(length);
        return true;
    }

    /**
     * Marshal the given typecode in a
     * <code>es.tid.TIDorbj.core.CDRInputStream</code>. This method will
     * alwais be invoked by this stream via the <code>TypeCodeMarshaler</code>.
     * 
     * @param type
     *            the <code>TypeCode</code>
     * @param output
     *            the <code>es.tid.TIDorbj.core.CDRInputStream</code>
     * @pre the <code>TypeCode</code> must be a fixed type
     */

    public static void marshal(TypeCode type, CDROutputStream output)
    {
        try {
            output.write_long(type.kind().value());
            output.write_ushort(type.fixed_digits());
            output.write_short(type.fixed_scale());
        }
        catch (BadKind bk) {
            throw new BAD_TYPECODE("Fault in Fixed operations.");
        }
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
     * @pre the <code>TypeCode</code> must be a fixed type
     */

    public static void remarshalValue(TypeCode type, InputStream input,
                                      OutputStream output)
    {
        int length = valueLength(type);
        for (int i = 0; i < length; i++)
            output.write_octet(input.read_octet());
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
     * @pre <code>type</code> must be a fixed type.
     */

    public static boolean valuesEqual(org.omg.CORBA.TypeCode type,
                                      InputStream input_a, InputStream input_b)
    {
        int length = valueLength(type);
        for (int i = 0; i < length; i++)
            if (input_a.read_octet() != input_b.read_octet())
                return false;
        return true;
    }

    /**
     * Dumps the description of a given TypeCode.
     * 
     * @param type
     *            the <code>TypeCode</code>
     * @param output
     *            the output stream where the TypeCode will be dumped
     * @pre <code>type</code> must be a fixed type.
     */

    public static void dump(org.omg.CORBA.TypeCode type,
                            java.io.PrintWriter output)
        throws java.io.IOException
    {
        try {
            output.print("[TYPECODE]{fixed<");
            output.print(type.fixed_digits());
            output.print(',');
            output.print(type.fixed_scale());
            output.print(">}");
        }
        catch (BadKind bk) {
            throw new BAD_TYPECODE("Fault in Fixed operations.");
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
     * @pre the typecode must be a fixed type
     * @return <code>true</code> if if has been possible dump the value.
     */

    public static boolean dumpValue(TypeCode type, InputStream input,
                                    java.io.PrintWriter output)
        throws java.io.IOException
    {
        output.print("[VALUE]{fixed: ");
        int length = valueLength(type);

        for (int i = 0; i < length; i++)
            output.print(input.read_octet());

        output.print("}");
        return true;
    }

}