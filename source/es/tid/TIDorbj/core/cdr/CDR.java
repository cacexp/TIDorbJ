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
package es.tid.TIDorbj.core.cdr;

/**
 * Collection of Basic Data type representation sizes in CDR.
 * 
 * @autor Juan A. C&aacute;ceres
 * @version 1.0
 */

public class CDR
{

    public final static int MAX_ALIGNMENT = 8;

    public final static int OCTET_SIZE = 1;

    public final static int BOOLEAN_SIZE = 1;

    public final static int CHAR_SIZE = 1;

    public final static int WCHAR_SIZE = 2;

    public final static int SHORT_SIZE = 2;

    public final static int USHORT_SIZE = 2;

    public final static int LONG_SIZE = 4;

    public final static int ULONG_SIZE = 4;

    public final static int FLOAT_SIZE = 4;

    public final static int LONGLONG_SIZE = 8;

    public final static int ULONGLONG_SIZE = 8;

    public final static int DOUBLE_SIZE = 8;

    //NOT IMPLEMENTED:
    //public final static int LONG_DOUBLE_SIZE = 16;

    public final static boolean BIG_ENDIAN = false;

    public final static boolean LITTLE_ENDIAN = true;

    public final static boolean LOCAL_BYTE_ORDER = BIG_ENDIAN;

    /**
     * Helper array to convert bytes to chars: the char is a component of the
     * array, indexed by the byte value.
     */
    public static final int[] CHAR_MAP = { 256 - 128, 256 - 127, 256 - 126,
                                          256 - 125, 256 - 124, 256 - 123,
                                          256 - 122, 256 - 121, 256 - 120,
                                          256 - 119, 256 - 118, 256 - 117,
                                          256 - 116, 256 - 115, 256 - 114,
                                          256 - 113, 256 - 112, 256 - 111,
                                          256 - 110, 256 - 109, 256 - 108,
                                          256 - 107, 256 - 106, 256 - 105,
                                          256 - 104, 256 - 103, 256 - 102,
                                          256 - 101, 256 - 100, 256 - 99,
                                          256 - 98, 256 - 97, 256 - 96,
                                          256 - 95, 256 - 94, 256 - 93,
                                          256 - 92, 256 - 91, 256 - 90,
                                          256 - 89, 256 - 88, 256 - 87,
                                          256 - 86, 256 - 85, 256 - 84,
                                          256 - 83, 256 - 82, 256 - 81,
                                          256 - 80, 256 - 79, 256 - 78,
                                          256 - 77, 256 - 76, 256 - 75,
                                          256 - 74, 256 - 73, 256 - 72,
                                          256 - 71, 256 - 70, 256 - 69,
                                          256 - 68, 256 - 67, 256 - 66,
                                          256 - 65, 256 - 64, 256 - 63,
                                          256 - 62, 256 - 61, 256 - 60,
                                          256 - 59, 256 - 58, 256 - 57,
                                          256 - 56, 256 - 55, 256 - 54,
                                          256 - 53, 256 - 52, 256 - 51,
                                          256 - 50, 256 - 49, 256 - 48,
                                          256 - 47, 256 - 46, 256 - 45,
                                          256 - 44, 256 - 43, 256 - 42,
                                          256 - 41, 256 - 40, 256 - 39,
                                          256 - 38, 256 - 37, 256 - 36,
                                          256 - 35, 256 - 34, 256 - 33,
                                          256 - 32, 256 - 31, 256 - 30,
                                          256 - 29, 256 - 28, 256 - 27,
                                          256 - 26, 256 - 25, 256 - 24,
                                          256 - 23, 256 - 22, 256 - 21,
                                          256 - 20, 256 - 19, 256 - 18,
                                          256 - 17, 256 - 16, 256 - 15,
                                          256 - 14, 256 - 13, 256 - 12,
                                          256 - 11, 256 - 10, 256 - 9, 256 - 8,
                                          256 - 7, 256 - 6, 256 - 5, 256 - 4,
                                          256 - 3, 256 - 2, 256 - 1, 0, 1, 2,
                                          3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
                                          14, 15, 16, 17, 18, 19, 20, 21, 22,
                                          23, 24, 25, 26, 27, 28, 29, 30, 31,
                                          32, 33, 34, 35, 36, 37, 38, 39, 40,
                                          41, 42, 43, 44, 45, 46, 47, 48, 49,
                                          50, 51, 52, 53, 54, 55, 56, 57, 58,
                                          59, 60, 61, 62, 63, 64, 65, 66, 67,
                                          68, 69, 70, 71, 72, 73, 74, 75, 76,
                                          77, 78, 79, 80, 81, 82, 83, 84, 85,
                                          86, 87, 88, 89, 90, 91, 92, 93, 94,
                                          95, 96, 97, 98, 99, 100, 101, 102,
                                          103, 104, 105, 106, 107, 108, 109,
                                          110, 111, 112, 113, 114, 115, 116,
                                          117, 118, 119, 120, 121, 122, 123,
                                          124, 125, 126, 127 };
}