/*
* MORFEO Project
* http://www.morfeo-project.org
*
* Component: TIDorbJ
* Programming Language: Java
*
* File: $Source$
* Version: $Revision: 478 $
* Date: $Date: 2011-04-29 16:42:47 +0200 (Fri, 29 Apr 2011) $
* Last modified by: $Author: avega $
*
* (C) Copyright 2004 Telef�nica Investigaci�n y Desarrollo
*     S.A.Unipersonal (Telef�nica I+D)
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

import org.omg.CORBA.INTERNAL;

import es.tid.TIDorbj.core.cdr.BufferCDR;
import es.tid.TIDorbj.core.cdr.CDRInputStream;
import es.tid.TIDorbj.core.cdr.CDROutputStream;
import es.tid.TIDorbj.core.comm.Connection;

/**
 * Base class for fragmented messages in 1.1 and 1.2 GIOP versions.
 * 
 * @see es.tid.TIDorbj.core.comm.iiop.GIOPMessage
 * 
 * 
 * @autor Juan A. C&aacute;ceres
 * @version 1.0
 */

public abstract class GIOPFragmentedMessage extends GIOPMessage
{

    /**
     * the request id, it can be needed by 1.1 and 1.2 GIOP version messages.
     */
    protected RequestId m_request_id;

    /**
     * InputStream where the message body is marshaled. It manages the body
     * buffer, defined in the <code>GIOPMessage</code> class.
     */
    protected CDRInputStream m_message_buffer_in;

    /**
     * OutputStream where the message message_buffer can be marshaled. It
     * manages the message_buffer buffer, defined in the
     * <code>GIOPMessage</code> class.
     */
    protected CDROutputStream m_message_buffer_out;

    private int fragmentSize;

    /**
     * Creates a new fragmented message for GIOP 1.0 and 1.1
     */
    protected GIOPFragmentedMessage(GIOPHeader header)
    {
        super(header);
        m_request_id = null;
    }

    /**
     * Creates a new fragmented message for GIOP 1.2
     */
    protected GIOPFragmentedMessage(GIOPHeader header, RequestId request_id, int fragmentSize )
    {
        super(header);
        this.m_request_id = request_id;
        this.fragmentSize = fragmentSize;
    }

    public String toString()
    {
        return m_header.toString() + " (" + m_request_id + ")";
    }

    public RequestId getRequestId()
    {
        return m_request_id;
    }

    /**
     * Initialize the message_buffer_out member.
     * 
     * @param orb
     *            needed for internal CDR stream creation.
     */

    protected void createMessageBufferOutput(es.tid.TIDorbj.core.TIDORB orb)
    {
        if (m_message_buffer == null)
            m_message_buffer = new BufferCDR( this.fragmentSize );
        else
            m_message_buffer.recycle();

        m_message_buffer_out = new CDROutputStream(orb, m_message_buffer);

        m_message_buffer_out.setVersion(m_header.getVersion());

        m_message_buffer_out.setMessage(true);

        // set message skips also the GIOP HEADER
        // message_buffer_out.skip(GIOPHeader.HEADER_SIZE);

        // writes the request id

        if (m_header.getVersion().equal(GIOPVersion.VERSION_1_2)) {
            m_message_buffer_out.write_ulong(m_request_id.value());
        }
    }

    /**
     * Initialize the message_buffer_out member.
     * 
     * @param orb
     *            needed for internal CDR stream creation.
     */

    protected void createMessageBufferInput(es.tid.TIDorbj.core.TIDORB orb)
    {
        if (m_message_buffer_in != null)
            return;

        if (m_message_buffer == null)
            throw new INTERNAL("Cannot create buffer input");

        m_message_buffer_in = new CDRInputStream(orb, m_message_buffer);

        m_message_buffer_in.setByteOrder(m_header.getByteOrder());

        m_message_buffer_in.setVersion(m_header.getVersion());

        m_message_buffer_in.setMessage(true);

        // set message skips also the GIOP HEADER
        // message_buffer_in.skip(GIOPHeader.HEADER_SIZE);

        // lectura adelantada del request_id para comprobar despues los
        // fragmentos
        if (m_header.getVersion().equal(GIOPVersion.VERSION_1_2)) {
            m_request_id = new RequestId(m_message_buffer_in.read_ulong());
        }

    }

    /**
     * Reads the message message_buffer in the connection. The header has yet
     * been read by the connection.
     * 
     * @param conn
     *            the active IIOP connection.
     */

    //TODO: giop should not know anything about IIOPConnections!!
    public void receiveBody(Connection conn, byte[] header_bytes)
    {
        super.receiveBody(conn, header_bytes);

        this.createMessageBufferInput(conn.orb());

        m_message_completed = !m_header.hasMoreFragments();
    }



    public void setBody(BufferCDR buf, CDRInputStream input)
    {
        super.setBody(buf);

        m_message_buffer_in = input;

        m_message_buffer_in.setByteOrder(m_header.getByteOrder());

        m_message_buffer_in.setVersion(m_header.getVersion());

        m_message_buffer_in.setMessage(true);


        // lectura adelantada del request_id para comprobar despues los
        // fragmentos
        if (m_header.getVersion().equal(GIOPVersion.VERSION_1_2)) {
            m_request_id = new RequestId(m_message_buffer_in.read_ulong());
        }

        m_message_completed = !m_header.hasMoreFragments();
    }


    /**
     * Complete writing of the message in the connection.
     */

    public void writeHeaders()
    {

        if (!m_headers_marshaled) {
            CDROutputStream out = new CDROutputStream(null, m_message_buffer);

            out.setVersion(m_header.getVersion());

            // write message size = buffer size - 12 octets from header

            m_header.setSize(m_message_buffer.getAvailable()
                             - GIOPHeader.HEADER_SIZE);

            m_header.setMoreFragments(false);

            m_header.write(out);

            out = null;
            m_headers_marshaled = true;
        }
    }

    /*
     * public void write_headers() { if (message_buffer == null) throw new
     * INTERNAL("Cannot write message headers");
     * 
     * switch(header.getVersion().getMinor()) { case 0: write_headers_1_0();
     * break; default: write_headers_1_1(); } }
     * 
     * public void write_headers_1_0() {
     * 
     * if (!headers_marshaled) { CDROutputStream out = new CDROutputStream(null,
     * message_buffer);
     * 
     * out.set_version(header.getVersion()); // write message size = buffer size -
     * 12 octets from header
     * 
     * header.setSize(message_buffer.getChunk(0).getAvailable() -
     * GIOPHeader.HEADER_SIZE);
     * 
     * header.setMoreFragments(false);
     * 
     * header.write(out);
     * 
     * out = null; headers_marshaled = true; } }
     * 
     * public void write_headers_1_1() {
     * 
     * if (!headers_marshaled) { CDROutputStream out = new CDROutputStream(null,
     * message_buffer);
     * 
     * out.set_version(header.getVersion()); // write message size = buffer size -
     * 12 octets from header
     * 
     * header.setSize(message_buffer.getChunk(0).getAvailable() -
     * GIOPHeader.HEADER_SIZE); // write chunks as fragments
     * 
     * int num_chunks = message_buffer.getNumAvailableChunks();
     * 
     * boolean has_more_fragments = num_chunks > 1;
     * 
     * header.setMoreFragments(has_more_fragments);
     * 
     * header.write(out);
     * 
     * if(has_more_fragments) {
     * 
     * out.set_message(true);
     * 
     * GIOPHeader fragment_header = new GIOPHeader(header.getVersion(),
     * MsgType.Fragment);
     * 
     * fragment_header.setByteOrder(header.getByteOrder());
     * 
     * for(int i = 1; i < num_chunks; i++) { out.getNextFragmentHeader();
     * fragment_header.setMoreFragments(i < num_chunks - 1);
     * fragment_header.setSize( message_buffer.getChunk(i).getAvailable() -
     * GIOPHeader.HEADER_SIZE);
     * 
     * GIOPFragmentMessage.write_header(out,fragment_header,request_id); } }
     * 
     * out = null; headers_marshaled = true; } }
     */

    /**
     * Adds the next fragment to the Message.
     */
    public void addFragment(GIOPFragmentMessage fragment)
    {
        if (m_message_completed)
            throw new org.omg.CORBA.MARSHAL("Unexpected Fragment");

        if (m_message_buffer == null)
            throw new org.omg.CORBA.INTERNAL("Unexpected Fragment");

        if (m_message_buffer != null)
            m_message_buffer.addChunk(fragment.getMessageBuffer().getChunk(0));

        m_message_completed = !fragment.getHeader().hasMoreFragments();

    }
}
