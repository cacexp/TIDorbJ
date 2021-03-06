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
package es.tid.TIDorbj.util;

/**
 * BASE64 encoding encodes 3 bytes into 4 characters.
 * |11111122|22223333|33444444| Each set of 6 bits is encoded according to the
 * toBase64 map. If the number of input bytes is not a multiple of 3, then the
 * last group of 4 characters is padded with one or two = signs. Each output
 * line is at most 76 characters.
 */

public class Base64Codec
{

    private final static int BYTE_GROUP = 3;

    private final static int CHAR_GROUP = 4;

    public static String encode(byte[] data)
    {
        int char_buff_length = (data.length / BYTE_GROUP) * CHAR_GROUP;

        if ((data.length % BYTE_GROUP) != 0)
            char_buff_length += CHAR_GROUP;

        char[] char_buff = new char[char_buff_length];

        int i = 0;
        int remain = data.length;
        for (int j = 0; j < data.length; j += BYTE_GROUP) {

            if (remain >= 3) {
                char_buff[i++] = toBase64[(data[j] & 0xFC) >> 2];
                char_buff[i++] = toBase64[((data[j] & 0x03) << 4)
                                          | ((data[j + 1] & 0xF0) >> 4)];
                char_buff[i++] = toBase64[((data[j + 1] & 0x0F) << 2)
                                          | ((data[j + 2] & 0xC0) >> 6)];
                char_buff[i++] = toBase64[data[j + 2] & 0x3F];
            } else if (remain == 1) {
                char_buff[i++] = toBase64[(data[j] & 0xFC) >> 2];
                char_buff[i++] = toBase64[(data[j] & 0x03) << 4];
                char_buff[i++] = '=';
                char_buff[i++] = '=';
            } else if (remain == 2) {
                char_buff[i++] = toBase64[(data[j] & 0xFC) >> 2];
                char_buff[i++] = toBase64[((data[j] & 0x03) << 4)
                                          | ((data[j + 1] & 0xF0) >> 4)];
                char_buff[i++] = toBase64[(data[j + 1] & 0x0F) << 2];
                char_buff[i++] = '=';
            }
            remain -= BYTE_GROUP;
        }

        return new String(char_buff);
    }

    public final static byte[] decode(String str)
        throws Exception
    {
        if ((str.length() % CHAR_GROUP) != 0)
            throw new 
            	Exception("Invalid string length, it must be multiple of 4");

        char[] char_buff = str.toCharArray();

        int data_chars = char_buff.length;

        while (char_buff[data_chars - 1] == '=')
            data_chars--;

        int whites = char_buff.length - data_chars;

        int data_bytes = (data_chars / CHAR_GROUP) * BYTE_GROUP;

        if (whites == 1)
            data_bytes += 2;
        else if (whites == 2)
            data_bytes += 1;

        byte[] data = new byte[data_bytes];

        int[] group_value = new int[CHAR_GROUP];

        int i = 0;

        int remain = data_bytes;

        for (int j = 0; j < char_buff.length; j += CHAR_GROUP) {
            if (remain >= 3) {
                group_value[0] = position(char_buff[j]);
                group_value[1] = position(char_buff[j + 1]);
                group_value[2] = position(char_buff[j + 2]);
                group_value[3] = position(char_buff[j + 3]);

                data[i++] = (byte) ((group_value[0] << 2) 
                    		| (group_value[1] >> 4));
                data[i++] = (byte) ((group_value[1] << 4) 
                    		| (group_value[2] >> 2));
                data[i++] = (byte) ((group_value[2] << 6) 
                    		| group_value[3]);
            } else if (remain == 2) {
                group_value[0] = position(char_buff[j]);
                group_value[1] = position(char_buff[j + 1]);
                group_value[2] = position(char_buff[j + 2]);

                data[i++] = (byte) ((group_value[0] << 2) 
                    		| (group_value[1] >> 4));
                data[i++] = (byte) ((group_value[1] << 4) 
                    		| (group_value[2] >> 2));
            } else if (remain == 1) {
                group_value[0] = position(char_buff[j]);
                group_value[1] = position(char_buff[j + 1]);

                data[i++] = (byte) ((group_value[0] << 2)
                    		| (group_value[1] >> 4));
            }

            remain -= BYTE_GROUP;
        }

        return data;
    }

    private final static int position(char value)
        throws Exception
    {
        if ((value >= 'A') && (value <= 'Z')) {
            return value - 'A';
        } else if ((value >= 'a') && (value <= 'z')) {
            return value - 'a' + 26;
        } else if ((value >= '0') && (value <= '9')) {
            return value - '0' + 52;
        } else if (value == '+') {
            return 62;
        } else if (value == '/') {
            return 63;
        } else if (value == '=') {
            return -1;
        } else
            throw new Exception("Invalid character in string");
    }

    private final static char[] toBase64 = 
    	{ 'A', 'B', 'C', 'D', 'E', 'F', 'G',
    	  'H', 'I', 'J', 'K', 'L', 'M', 'N',
    	  'O', 'P', 'Q', 'R', 'S', 'T', 'U',
    	  'V', 'W', 'X', 'Y', 'Z', 'a', 'b',
    	  'c', 'd', 'e', 'f', 'g', 'h', 'i',
    	  'j', 'k', 'l', 'm', 'n', 'o', 'p',
    	  'q', 'r', 's', 't', 'u', 'v', 'w',
    	  'x', 'y', 'z', '0', '1', '2', '3',
    	  '4', '5', '6', '7', '8', '9', '+',
    	  '/' };

    /*
     * public static void main(String[] args) { for(int i = 0; i <
     * toBase64.length; i++) { System.out.println("Value ("+i +") of " +
     * toBase64[i] + " is " + ((int)toBase64[i])); }
     * 
     * try { String str = "Hola cara-cola in the morning";
     * 
     * System.out.println("Initial " + str);
     * 
     * byte[] data = str.getBytes();
     * 
     * String encoded = encode(data);
     * 
     * System.out.println("Encoded " + encoded);
     * 
     * byte[] decoded = decode(encoded);
     * 
     * java.io.ByteArrayOutputStream buffer = new
     * java.io.ByteArrayOutputStream();
     * 
     * buffer.write(decoded,0, decoded.length);
     * 
     * String str_2 = buffer.toString();
     * 
     * System.out.println("Processed " + str_2); } catch (Throwable e) {
     * e.printStackTrace(); } }
     */
}