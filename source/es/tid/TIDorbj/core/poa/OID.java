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
package es.tid.TIDorbj.core.poa;

import es.tid.TIDorbj.util.Base64Codec;

/**
 * General Representation of an Object Indentifier.
 * 
 * @author Juan A. C&aacute;ceres
 * @version 1.0
 */
public class OID
{

    protected byte[] m_value;

    protected int m_hash_code;

    protected boolean m_hash_created;

    protected String m_str;

    protected OID()
    {
        m_value = null;
        m_hash_code = 0;
        m_hash_created = false;
    }

    public OID(byte[] val)
    {
        this();
        m_value = val;
    }

    public byte[] toByteArray()
    {
        return m_value;
    }

    public boolean equals(Object obj)
    {
        OID other = null;

        if (!(obj instanceof OID))
            return false;

        other = (OID) obj;

        if (m_value == null) {
            if (other.m_value == null)
                return true;
            else
                return false;
        }

        if (m_value.length != other.m_value.length)
            return false;

        for (int i = 0; i < m_value.length; i++) {
            if (m_value[i] != other.m_value[i])
                return false;
        }

        return true;
    }

    public int hashCode()
    {
        if (!m_hash_created) {

            if (m_value == null)
                return 0;

            for (int i = 0; i < m_value.length; i++)
                m_hash_code = (31 * m_hash_code) + m_value[i];
            m_hash_created = true;
        }

        return m_hash_code;
    }

    public static OID fromString(String str)
        throws Exception
    {
        return new OID(Base64Codec.decode(str));
    }

    public String toString()
    {
        if (m_str == null) {
            if ((m_value != null) || (m_value.length > 0))
                m_str = Base64Codec.encode(m_value);
            else
                m_str = "";
        }

        return m_str;
    }

    /*
     * public static void main (String[] args) { try {
     * 
     * byte[] bytes = {(byte)0,(byte)0,(byte)0,(byte)0,(byte)1,(byte)23};
     * 
     * OID oid = new OID(bytes);
     * 
     * String str = oid.toString();
     * 
     * System.out.println(str);
     * 
     * oid = OID.fromString(str);
     * 
     * 
     * byte[] oid_bytes = oid.toByteArray();
     * 
     * for(int i = bytes.length -1 ; i >= 0; i--) if(oid_bytes[i] != bytes[i])
     * System.out.println("Error in bytes");
     * 
     * System.out.println("final"); } catch (Throwable th)
     * {th.printStackTrace();} }
     *  
     */
}

