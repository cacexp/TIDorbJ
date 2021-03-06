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
package es.tid.TIDorbj.core.comm.iiop;

import java.net.Socket;
import javax.net.ssl.SSLSocket;

import org.omg.BiDirPolicy.BidirectionalPolicy;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.TRANSIENT;

import es.tid.TIDorbj.core.TIDORB;
import es.tid.TIDorbj.core.poa.POAImpl;
import es.tid.TIDorbj.core.policy.PolicyContext;
import es.tid.TIDorbj.core.util.UseTable;
import es.tid.TIDorbj.util.Trace;
import es.tid.TIDorbj.core.comm.Connection;
import es.tid.TIDorbj.core.comm.iiop.IIOPConnection;
import es.tid.TIDorbj.core.comm.ssliop.SSLConnection;

/**
 * Manages the opened connections. When a connection is needed, try to reuse an
 * opened one. When there is too many connection opened or there are some
 * connection that have remained idle too long, they will be closed.
 * 
 * @author Juan A. C&aacute;ceres
 * @version 1.0
 */


// TODO: move comm.iiop.IIOPConnectionManager to comm.ConnectionManager

public class IIOPConnectionManager extends es.tid.TIDorbj.core.ORBComponent
{

    /**
     * The communication Layer.
     */

	IIOPCommLayer commLayer;

    /**
     * The ORB has been destroyed.
     */

    protected boolean m_destroyed;

    /**
     * Maintains a <code>UseTable</code> with <code>IIOPConnection</code>
     * objects. Then, when the table removes older connections, the manager
     * close them.
     */
    UseTable m_connections;

    /**
     * Opened client connections. This table allows reuse opened connections for
     * a given listen point.
     * <p>
     * This is a <code>Hashtable</code>, the stored object class is
     * <code>IIOPConnection</code> and the keys are <code>ListenPoint</code>s.
     */

    java.util.Hashtable m_client_connections;

    /**
     * Opened bidirectional connections. This table allows reuse opened
     * connections for a given listen point.
     * <p>
     * This is a <code>Hashtable</code>, the stored object class is
     * <code>IIOPConnection</code> and the keys are <code>ListenPoint</code>s.
     */

    java.util.Hashtable m_bidirectional_connections;

    /**
     * Connections that are opening now. Other threads that want to open a new
     * connection to the same listen point must wait to not open more than one
     * connection.
     */
    java.util.Hashtable m_connections_opening;

    int maxOpenedConnections;
    int connectTimeout;
    boolean ipv6;
    

    /**
     * Opened SSL client connections. This table allows reuse opened connections for
     * a given listen point.
     * <p>
     * This is a <code>Hashtable</code>, the stored object class is
     * <code>IIOPConnection</code> and the keys are <code>ListenPoint</code>s.
     */

    java.util.Hashtable m_ssl_client_connections;

    public IIOPConnectionManager( TIDORB orb, IIOPCommLayer commLayer )
    {
        super(orb);
        this.commLayer = commLayer;
        m_client_connections = new java.util.Hashtable();
        m_bidirectional_connections = new java.util.Hashtable();
        m_ssl_client_connections = new java.util.Hashtable();
        
        maxOpenedConnections = 
            m_orb.getCommunicationManager().getLayerById( IIOPCommunicationLayer.ID )
            .getPropertyInfo( IIOPCommunicationLayerPropertiesInfo.MAX_OPENED_CONNECTIONS )
            .getInt(); 
        
        connectTimeout = 
            m_orb.getCommunicationManager().getLayerById( IIOPCommunicationLayer.ID )
            .getPropertyInfo( IIOPCommunicationLayerPropertiesInfo.SOCKET_CONNECT_TIMEOUT )
            .getInt(); 

        
        m_connections = new UseTable( maxOpenedConnections );
        
        
        m_connections_opening = new java.util.Hashtable();
        m_destroyed = false;
        
        //mcpg
        this.ipv6 =
            m_orb.getCommunicationManager().getLayerById( IIOPCommunicationLayer.ID )
            .getPropertyInfo( IIOPCommunicationLayerPropertiesInfo.IPV6 )
            .getBoolean();
        if (this.ipv6 == true)
        {
          System.setProperty("java.net.preferIPv6Address","true");
          System.setProperty("java.net.preferIPv4Stack","false");
        }
    }

    /**
     * Notifies the connection use.
     * 
     * @param conn
     *            the connection in use
     */
    /* synchronized */

    //public synchronized void use(IIOPConnection conn)
    public synchronized void use(Connection conn)
    {
        if (m_destroyed)
            return;

        m_connections.use(conn);
    }

    /**
     * The manager is Notified a connection is been closing.
     * 
     * @param conn
     *            the connection that is closing.
     */

    //synchronized public void closing(IIOPConnection conn)
    synchronized public void closing(Connection conn)
    {
        if (m_destroyed)
            return;

        removeListenPoints(conn);

        m_connections.remove(conn);
    }

    /**
     * When a new Socket is created (a new connection has been accepted by the
     * <code>ServerSocket</code>) a new IIOPConnection, in SERVER mode, must be
     * registered.
     * 
     * @param socket
     *            the new socket.
     */

    synchronized public void createServerConnection(Socket socket)
    {
        if (m_destroyed) {
            // drop the socket
            try {
                socket.close();
            }
            catch (java.io.IOException ioe) {}
        } else {
            // create the new connection and add the new connection to
            // connection table
            newConnection(IIOPConnection.serverConnection(this, socket));
        }
    }


    /**
     * Saves the <code>ListenPoint</code> associated to a bidirectional
     * connection to be used as a client connection when a connection to the
     * listen point will be required.
     * 
     * @param listen_point
     *            the <code>ListenPoint</code> where the connection will be
     *            seen as a client connection.
     * @param conn
     *            the bidirectional connection.
     */

    synchronized public void
    	addBidirectionalConnection(ListenPoint listen_point,
//    	                           IIOPConnection conn)
    	                           Connection conn)
    {
        if (m_destroyed)
            return;

        m_bidirectional_connections.put(listen_point, conn);
    }

    /**
     * Looks for a client connection with the listen point. If it does not
     * exist, then the creates one.
     * 
     * @param listen_point
     *            the <code>ListenPoint</code> that determines a remote ORB in
     *            a Object reference.
     */
    public IIOPConnection getClientConnection(ListenPoint listen_point,
                                          PolicyContext policy_context)
    {
        //opened connection
        IIOPConnection conn = null;
        // check if a connection is opening now
        OpeningLock opening_lock = null;
        // this thread must open a connection and unlock the OpeningLock
        boolean open_a_connection = false;

        synchronized (this) {
            if (m_destroyed)
                throw new TRANSIENT("IIOP Layer shutdown", 0,
                                    CompletionStatus.COMPLETED_NO);

            // looks for an existing connection

            conn = (IIOPConnection) m_bidirectional_connections.get(listen_point);

            if (conn != null)
                return conn;

            conn = (IIOPConnection) m_client_connections.get(listen_point);

            if (conn != null)
                return conn;

            // check if is opening now
            opening_lock = (OpeningLock) 
            	m_connections_opening.get(listen_point);

            // create a lock
            if (opening_lock == null) {
                opening_lock = new OpeningLock();
                m_connections_opening.put(listen_point, opening_lock);
                open_a_connection = true;
            }
        }

        if (!open_a_connection) {
            try {
                conn = (IIOPConnection)opening_lock.waitOpening( this.connectTimeout );
            } catch (org.omg.CORBA.COMM_FAILURE ce) {
                synchronized (this) {
                    if (m_orb.m_trace != null) {
                        String[] msg = { "Socket connection timeout exceeded",
                                         "waiting to connecting with ", 
                                         listen_point.toString(), 
                                         ". Released lock over it." };
                        m_orb.printTrace(Trace.DEBUG, msg);
                    }
                    m_connections_opening.remove(listen_point);
                    throw new TRANSIENT(ce.toString());
                }
            }

        } else {
            try {
                conn = openClientConnection(listen_point, policy_context);
            } catch (org.omg.CORBA.COMM_FAILURE ce) {
                synchronized (this) {
                    opening_lock.setError(ce);
                    m_connections_opening.remove(listen_point);
                    throw ce;
                }
            }

            synchronized (this) {

                newConnection(conn);

                // add the new connection to the client_connections

                m_client_connections.put(listen_point, conn);

                opening_lock.setOpened(conn);
                m_connections_opening.remove(listen_point);
            }

            if (m_orb.m_trace != null) {
                String[] msg = { conn.toString(), " Opened!" };
                m_orb.printTrace(Trace.DEBUG, msg);
            }

        }
        
        BidirectionalPolicy bidir = policy_context.getBidirectionalPolicy();
                
        if ( (!conn.isBidirectionalConnection())
            && (bidir != null)
            && (bidir.value() == org.omg.BiDirPolicy.BOTH.value)
            && (commLayer.hasServerListener())) {
            conn.setBidirectionalMode(
            		commLayer.getBidirectionalService()
			);
        }

        return conn;
    }

    private IIOPConnection 
    	openClientConnection(ListenPoint listen_point,
    	                     PolicyContext policy_context)
    {
        IIOPConnection conn = null;

        // create a new connection
        if (m_orb.m_trace != null) {
            String[] msg = { "Opening client connection with ",
                            listen_point.toString() };
            m_orb.printTrace(Trace.DEBUG, msg);
        }
        // create the new connection
        conn = IIOPConnection.clientConnection(this, listen_point);

        // is bidirectional ???

        BidirectionalPolicy bidir = policy_context.getBidirectionalPolicy();
        
        if ((bidir != null && bidir.value()
            == org.omg.BiDirPolicy.BOTH.value)
            && (commLayer.hasServerListener())) {
            conn.setBidirectionalMode(commLayer.getBidirectionalService());
        } else {

            es.tid.TIDorbj.core.poa.CurrentImpl poa_current = 
                m_orb.initPOACurrent();

            if (poa_current.inContext()) {
                try {
                    POAImpl current_poa = (POAImpl) poa_current.get_POA();

                    if (current_poa.isBidirectional())
                        conn.setBidirectionalMode(
                           commLayer.getBidirectionalService());

                }
                catch (org.omg.PortableServer.CurrentPackage.NoContext nc) {}
                // add the new connection to connection table
            }
        }

        return conn;
    }


    private SSLConnection 
    	openSSLClientConnection(ListenPoint listen_point,
    	                     PolicyContext policy_context)
    {
        SSLConnection conn = null;

        // create a new connection
        if (m_orb.m_trace != null) {
            String[] msg = { "Opening client connection with ",
                            listen_point.toString() };
            m_orb.printTrace(Trace.DEBUG, msg);
        }
        // create the new connection
        conn = SSLConnection.clientConnection(this, listen_point);

        // is bidirectional ???

        BidirectionalPolicy bidir = policy_context.getBidirectionalPolicy();
        
        if ((bidir != null && bidir.value()
            == org.omg.BiDirPolicy.BOTH.value)
            && (commLayer.hasServerListener())) {
            conn.setBidirectionalMode(commLayer.getBidirectionalService());
        } else {

            es.tid.TIDorbj.core.poa.CurrentImpl poa_current = 
                m_orb.initPOACurrent();

            if (poa_current.inContext()) {
                try {
                    POAImpl current_poa = (POAImpl) poa_current.get_POA();

                    if (current_poa.isBidirectional())
                        conn.setBidirectionalMode(
                           commLayer.getBidirectionalService());

                }
                catch (org.omg.PortableServer.CurrentPackage.NoContext nc) {}
                // add the new connection to connection table
            }
        }

        return conn;
    }

    /**
     * Looks for a client connection with the listen point. If it does not
     * exist, then the creates one.
     * 
     * @param listen_point
     *            the <code>ListenPoint</code> that determines a remote ORB in
     *            a Object reference.
     */
    public void prepareClientConnection(ListenPoint listen_point,
                                        PolicyContext policy_context)
    {
        getClientConnection(listen_point, policy_context);
    }

    /**
     * Checks in a new connection for manage it.
     * 
     * @param conn
     *            the bidirectional connection.
     */
    protected void newConnection(IIOPConnection conn)
    {
        try {

            m_connections.append(conn);

        } catch (es.tid.TIDorbj.core.util.FullUseTableException f) {
            conn.closeByManager();
            if (conn.isClientConnection()) {
                throw new TRANSIENT(
                    "Maximun of Connections reached, and all are in use, try later!!",
                    0, 
					CompletionStatus.COMPLETED_NO
				);
            }
        }

        Object[] removed = m_connections.getRemovedObjects();

        if (removed != null) {
            for (int i = 0; i < removed.length; i++) {
                closeConnection((IIOPConnection) removed[i]);
            }
        }
    }



    /**
     * Checks in a new connection for manage it.
     * 
     * @param conn
     *            the bidirectional connection.
     */
    protected void newConnection(SSLConnection conn)
    {
        try {

            m_connections.append(conn);

        } catch (es.tid.TIDorbj.core.util.FullUseTableException f) {
            conn.closeByManager();
            if (conn.isClientConnection()) {
                throw new TRANSIENT(
                    "Maximun of Connections reached, and all are in use, try later!!",
                    0, 
					CompletionStatus.COMPLETED_NO
				);
            }
        }

        Object[] removed = m_connections.getRemovedObjects();

        if (removed != null) {
            for (int i = 0; i < removed.length; i++) {
                closeConnection((IIOPConnection) removed[i]);
            }
        }
    }

    /**
     * Removes from the client connection table the listen points associated to
     * a connection.
     * 
     * @param conn
     *            the connection.
     */
    //protected void removeListenPoints(IIOPConnection conn)
    protected void removeListenPoints(Connection conn)
    {
        java.util.Enumeration points = conn.getListenPoints();
        if (points == null)
            return;

        // remove listenPoints
        while (points.hasMoreElements()) {

            ListenPoint _listen_point = (ListenPoint) points.nextElement();

            // remove from client_connections
            m_client_connections.remove(_listen_point);

            // remove from bidirectional_connections if ListenPoint refers to
            // the given connection
            //if ((IIOPConnection) m_bidirectional_connections.get(_listen_point)
            if ((Connection) m_bidirectional_connections.get(_listen_point)
                == conn)
                m_bidirectional_connections.remove(_listen_point);
        }
    }

    /**
     * Close the connection due to it has been decided that it not has been used
     * for a long time.
     * 
     * @param conn
     *            the connection.
     */
    //protected void closeConnection(IIOPConnection conn)
    protected void closeConnection(Connection conn)
    {
        removeListenPoints(conn);
        conn.closeByManager();
    }

    /**
     * @return <code>true</code> if it has not any active connection
     */
    public boolean activeConnections()
    {
        return m_connections.getSize() > 0;
    }

    /**
     * Close all connections due to an ORB close session.
     */
    synchronized public void destroy()
    {
        if (!m_destroyed) {
            java.util.Enumeration conns = m_connections.elements();

            //IIOPConnection conn;
            Connection conn;

            while (conns.hasMoreElements()) {
                conn = (IIOPConnection) conns.nextElement();
                m_connections.remove(conn);
                closeConnection(conn);
            }

            m_destroyed = true;
        }
    }




    /**
     * When a new SSLSocket is created (a new connection has been accepted by the
     * <code>ServerSocket</code>) a new IIOPConnection, in SERVER mode, must be
     * registered.
     * 
     * @param socket
     *            the new socket.
     */

    synchronized public void createSSLServerConnection(SSLSocket socket)
    {
        if (m_destroyed) {
            // drop the socket
            try {
                socket.close();
            }
            catch (java.io.IOException ioe) {}
        } else {
            // create the new connection and add the new connection to
            // connection table
            newConnection(SSLConnection.serverConnection(this, socket));
        }
    }

    /**
     * Looks for a client connection with the listen point. If it does not
     * exist, then the creates one.
     * 
     * @param listen_point
     *            the <code>ListenPoint</code> that determines a remote ORB in
     *            a Object reference.
     */
    public SSLConnection getSSLClientConnection(ListenPoint listen_point,
                                                PolicyContext policy_context)
    {
        //opened connection
        SSLConnection conn = null;
        // check if a connection is opening now
        OpeningLock opening_lock = null;
        // this thread must open a connection and unlock the OpeningLock
        boolean open_a_connection = false;

        synchronized (this) {
            if (m_destroyed)
                throw new TRANSIENT("IIOP Layer shutdown", 0,
                                    CompletionStatus.COMPLETED_NO);

            // looks for an existing connection

//             conn = (IIOPConnection) m_bidirectional_connections.get(listen_point);

//             if (conn != null)
//                 return conn;

            conn = (SSLConnection) m_ssl_client_connections.get(listen_point);

            if (conn != null)
                return conn;

            // check if is opening now
            opening_lock = (OpeningLock) 
            	m_connections_opening.get(listen_point);

            // create a lock
            if (opening_lock == null) {
                opening_lock = new OpeningLock();
                m_connections_opening.put(listen_point, opening_lock);
                open_a_connection = true;
            }
        }

        if (!open_a_connection) {
            try {
                conn = (SSLConnection)opening_lock.waitOpening( this.connectTimeout );
            } catch (org.omg.CORBA.COMM_FAILURE ce) {
                synchronized (this) {
                    if (m_orb.m_trace != null) {
                        String[] msg = { "Socket connection timeout exceeded",
                                         "waiting to connecting with ", 
                                         listen_point.toString(), 
                                         ". Released lock over it." };
                        m_orb.printTrace(Trace.DEBUG, msg);
                    }
                    m_connections_opening.remove(listen_point);
                    throw new TRANSIENT(ce.toString());
                }
            }

        } else {
            try {
                conn = openSSLClientConnection(listen_point, policy_context);
            } catch (org.omg.CORBA.COMM_FAILURE ce) {
                synchronized (this) {
                    opening_lock.setError(ce);
                    m_connections_opening.remove(listen_point);
                    throw ce;
                }
            }

            synchronized (this) {

                newConnection(conn);

                // add the new connection to the client_connections

                m_ssl_client_connections.put(listen_point, conn);

                opening_lock.setOpened(conn);
                m_connections_opening.remove(listen_point);
            }

            if (m_orb.m_trace != null) {
                String[] msg = { conn.toString(), " Opened!" };
                m_orb.printTrace(Trace.DEBUG, msg);
            }

        }
        
//         BidirectionalPolicy bidir = policy_context.getBidirectionalPolicy();
                
//         if ( (!conn.isBidirectionalConnection())
//             && (bidir != null)
//             && (bidir.value() == org.omg.BiDirPolicy.BOTH.value)
//             && (commLayer.hasServerListener())) {
//             conn.setBidirectionalMode(
//             		commLayer.getBidirectionalService()
// 			);
//         }

        return conn;
    }


}
