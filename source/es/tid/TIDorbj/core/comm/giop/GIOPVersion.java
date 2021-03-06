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
package es.tid.TIDorbj.core.comm.giop;

import java.util.HashMap;

/**
 * GIOPVersion structure defined in the GIOP Module.
 * 
 * @autor Juan A. C&aacute;ceres
 * @version 1.0
 */

//TODO: GIOPVersion == GIOPVersion??
public class GIOPVersion
{
    public int major;

    public int minor;

    private GIOPVersion(int maj, int min)
    {
        major = maj;
        minor = min;
    }

    public int getMajor()
    {
        return major;
    }

    public int getMinor()
    {
        return minor;
    }

    public boolean equal(GIOPVersion ver)
    {
        return (major == ver.major) && (minor == ver.minor);
    }

    private static HashMap versionMap;
    public static GIOPVersion fromString( String version ){
        //jprojas: hehehe
        synchronized( GIOPVersion.class ){
            if ( versionMap == null ){
                versionMap = new HashMap( 3, 1 );
                versionMap.put( "1.0", VERSION_1_0 );
                versionMap.put( "1.1", VERSION_1_1 );
                versionMap.put( "1.2", VERSION_1_2 );
            }
        }
        //Hope casting doesn't cost too much
        return (GIOPVersion)versionMap.get( version );
        
        /*
        GIOPVersion iiopVersion;
        StringTokenizer st = new StringTokenizer( version, ".", false );
        if ( st.countTokens() == 2 ){
            try {
                int major = Integer.parseInt( st.nextToken() );
                int minor = Integer.parseInt( st.nextToken() );
                iiopVersion = fromInts( major, minor );
            } catch ( NumberFormatException nfe ){
                //Nothing can be done
                iiopVersion = null;
            }
        } else {
            iiopVersion = null;
        }
        return iiopVersion;
        */
    }    
    
    public static GIOPVersion fromInts(int major, int minor)
    {
        if (major != 1)
            return null;
        if (minor == 0)
            return VERSION_1_0;
        if (minor == 1)
            return VERSION_1_1;
        if (minor == 2)
            return VERSION_1_2;
        return null;
    }

    public static GIOPVersion read(org.omg.CORBA.portable.InputStream input)
    {
        int major = input.read_octet();
        int minor = input.read_octet();
        if (major != 1)
            return null;
        if (minor == 0)
            return VERSION_1_0;
        if (minor == 1)
            return VERSION_1_1;
        if (minor == 2)
            return VERSION_1_2;
        return null;
    }

    public void write(org.omg.CORBA.portable.OutputStream output)
    {
        output.write_octet((byte) major);
        output.write_octet((byte) minor);
    }

    public String toString()
    {
        if (minor == 0)
            return VERSION_1_0_NAME;
        if (minor == 1)
            return VERSION_1_1_NAME;
        else
            return VERSION_1_2_NAME;
    }

    public final static GIOPVersion VERSION_1_0 = new GIOPVersion(1, 0);

    public final static String VERSION_1_0_NAME = "GIOPVersion(1.0)";

    public final static GIOPVersion VERSION_1_1 = new GIOPVersion(1, 1);

    public final static String VERSION_1_1_NAME = "GIOPVersion(1.1)";

    public final static GIOPVersion VERSION_1_2 = new GIOPVersion(1, 2);

    public final static String VERSION_1_2_NAME = "GIOPVersion(1.2)";

}