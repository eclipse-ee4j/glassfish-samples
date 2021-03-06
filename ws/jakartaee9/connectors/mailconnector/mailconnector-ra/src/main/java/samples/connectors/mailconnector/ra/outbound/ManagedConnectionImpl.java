/*
 	Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
	
	This program and the accompanying materials are made available under the
	terms of the Eclipse Public License v. 2.0, which is available at
	http://www.eclipse.org/legal/epl-2.0.
	
	This Source Code may also be made available under the following Secondary
	Licenses when the conditions for such availability set forth in the
	Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
	version 2 with the GNU Classpath Exception, which is available at
	https://www.gnu.org/software/classpath/license.html.
	
	SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
*/

package samples.connectors.mailconnector.ra.outbound;

import jakarta.resource.spi.*;
import java.lang.Object;
import jakarta.resource.ResourceException;
import javax.transaction.xa.XAResource;
import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.*;
import java.util.logging.*;
import jakarta.resource.spi.security.PasswordCredential;
import jakarta.resource.spi.SecurityException;
import jakarta.resource.spi.IllegalStateException;
import jakarta.resource.NotSupportedException;

import samples.connectors.mailconnector.api.*;
import samples.connectors.mailconnector.share.*;

/**
 * The ManagedConnectionImpl class represents a physical connection to the
 * backend Mail Server.
 */

public class ManagedConnectionImpl implements ManagedConnection {
    private ManagedConnectionFactoryImpl    mcf;
    private JavaMailConnectionEventListener eventListener;
    // set of Mail Server Connections
    private Set  connectionSet;
    private PrintWriter   logWriter;
    private boolean    destroyed;
    private static int  testCounter = 0;
    // Several connections from a store
    private MailServerStore  store  = null;
    private int myId  = 0;

    private PasswordCredential              passCred    = null;

    static Logger logger = Logger.getLogger( "samples.connectors.mailconnector.ra.outbound",
                                             "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    ResourceBundle  resource = ResourceBundle.getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     * 
     * @param mcf
     *            the ManagedConnectionFactory that created this instance
     * @param subject
     *            security context as JAAS subject
     * @param cxRequestInfo
     *            ConnectionRequestInfo instance
     */

    ManagedConnectionImpl(ManagedConnectionFactoryImpl mcf, Subject subject,
            ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        myId = testCounter++;
        logger.fine(" 3B.- (" + myId + ") ManagedConnection::constructor");
        this.mcf = mcf;

        // Note: this will select the credential that matches this MC's MCF.
        // The credential's MCF is set by the application server.
        this.passCred = Util.getPasswordCredential(mcf, subject, cxRequestInfo);

        // Open the physical connection to a store

        openStore(cxRequestInfo);

        connectionSet = new HashSet();
        eventListener = new JavaMailConnectionEventListener(this);
    }

    /**
     * Creates a new connection handle to the Mail Server represented by the
     * ManagedConnection instance. This connection handle is used by the
     * application code to refer to the underlying physical connection.
     * 
     * @param subject
     *            security context as JAAS subject
     * @param cxRequestInfo
     *            ConnectionRequestInfo instance
     * 
     * @return Connection instance representing the connection handle
     * 
     * @exception ResourceException
     *                if the method fails to get a connection
     */

    public Object getConnection(Subject subject,
            ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        logger.fine(" 4.- ("
                + myId
                + ") testManagedConnection::getConnection: ConnectionManager requested a Connection handle");

        checkIfDestroyed();

        PasswordCredential pc = Util.getPasswordCredential(mcf, subject,
                cxRequestInfo);

        if (!Util.isPasswordCredentialEqual(pc, passCred)) {
            // logger.finest("getConnection: User authentication failed");
            throw new SecurityException(
                    resource.getString("PRINCIPAL_DOES_NOT_MATCH"));
        }

        // We only need the Folder name as all the connections share the store

        String folderName;

        if (cxRequestInfo != null) {
            folderName = ((ConnectionRequestInfoImpl) cxRequestInfo)
                    .getFolderName();
        } else {
            // Use default values
            folderName = mcf.getFolderName();
        }

        jakarta.mail.Folder folder;

        try {
            folder = store.getFolder(folderName);
        } catch (Exception e) {
            logger.warning("ManagedConnectionImpl::getConnection threw exception: "
                    + e);
            throw new ResourceException(e.getMessage());
        }

        JavaMailConnection javaMailCon = new JavaMailConnectionImpl(this,
                folder);
        addJavaMailConnection(javaMailCon);
        // logger.finest("getConnection: returning a Mail Server Connection handle to CM");
        return javaMailCon;
    }

    /**
     * Destroys the physical connection.
     * 
     * @exception ResourceException
     *                if the method fails to destroy the connection
     */

    public void destroy() throws ResourceException {
        if (destroyed)
            return;

        logger.fine(" 9.- (" + myId + ") ManagedConnection::destroy called");
        destroyed = true;

        testCounter--;

        try {
            store.closeStore();
        } catch (Exception e) {
            logger.warning("ManagedConnectionImpl::destroy threw exception: "
                    + e);
            throw new ResourceException(e.getMessage());
        }

    }

    /**
     * Initiates a cleanup of the client-specific state maintained by a
     * ManagedConnection instance. The cleanup should invalidate all connection
     * handles created using this ManagedConnection instance.
     * 
     * @exception ResourceException
     *                if the cleanup fails
     */

    public void cleanup() throws ResourceException {
        checkIfDestroyed();

        logger.fine(" 8.- (" + myId + ") ManagedConnection::cleanup called");

        invalidateJavaMailConnections();
    }

    private void invalidateJavaMailConnections() {
        Iterator it = connectionSet.iterator();
        while (it.hasNext()) {
            JavaMailConnectionImpl javaMailCon = (JavaMailConnectionImpl) it
                    .next();
            javaMailCon.invalidate();
        }
        connectionSet.clear();
    }

    /**
     * Used by the container to change the association of an application-level
     * connection handle with a ManagedConnection instance. The container should
     * find the right ManagedConnection instance and call the
     * associateConnection method.
     * 
     * @param connection
     *            application-level connection handle
     * 
     * @exception ResourceException
     *                if the attempt to change the association fails
     */

    public void associateConnection(Object connection) throws ResourceException {
        checkIfDestroyed();

        if (connection instanceof JavaMailConnection) {
            JavaMailConnectionImpl javaMailCon = (JavaMailConnectionImpl) connection;
            javaMailCon.associateConnection(this);
        } else {
            throw new IllegalStateException(
                    resource.getString("INVALID_CONNECTION"));
        }
    }

    /**
     * Adds a connection event listener to the ManagedConnection instance. The
     * registered ConnectionEventListener instances are notified of connection
     * close and error events as well as local-transaction-related events on the
     * Managed Connection.
     * 
     * @param listener
     *            a new ConnectionEventListener to be registered
     */

    public void addConnectionEventListener(ConnectionEventListener listener) {
        eventListener.addConnectorListener(listener);
    }

    /**
     * Removes an already registered connection event listener from the
     * ManagedConnection instance.
     * 
     * @param listener
     *            already registered connection event listener to be removed
     */

    public void removeConnectionEventListener(ConnectionEventListener listener) {
        eventListener.removeConnectorListener(listener);
    }

    /**
     * Returns a javax.transaction.xa.XAresource instance. An application server
     * enlists this XAResource instance with the Transaction Manager if the
     * ManagedConnection instance is being used in a JTA transaction that is
     * being coordinated by the Transaction Manager.
     * 
     * Because this implementation does not support transactions, the method
     * throws an exception.
     * 
     * @return the XAResource instance
     * 
     * @exception ResourceException
     *                if transactions are not supported
     */

    public XAResource getXAResource() throws ResourceException {
        throw new NotSupportedException(resource.getString("NO_XATRANSACTION"));
    }

    /**
     * Returns a jakarta.resource.spi.LocalTransaction instance. The
     * LocalTransaction interface is used by the container to manage local
     * transactions for a RM instance.
     * 
     * Because this implementation does not support transactions, the method
     * throws an exception.
     * 
     * @return jakarta.resource.spi.LocalTransaction instance
     * 
     * @exception ResourceException
     *                if transactions are not supported
     */

    public jakarta.resource.spi.LocalTransaction getLocalTransaction()
            throws ResourceException {
        throw new NotSupportedException(resource.getString("NO_TRANSACTION"));
    }

    /**
     * Gets the metadata information for this connection's underlying EIS
     * resource manager instance. The ManagedConnectionMetaData interface
     * provides information about the underlying EIS instance associated with
     * the ManagedConnection instance.
     * 
     * @return ManagedConnectionMetaData ManagedConnectionMetaData instance
     * 
     * @exception ResourceException
     *                if the metadata cannot be retrieved
     */

    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        checkIfDestroyed();
        return new ManagedConnectionMetaDataImpl(this);
    }

    /**
     * Sets the log writer for this ManagedConnection instance. The log writer
     * is a character output stream to which all logging and tracing messages
     * for this ManagedConnection instance will be printed.
     * 
     * @param out
     *            character output stream to be associated
     * 
     * @exception ResourceException
     *                if the method fails
     */

    public void setLogWriter(PrintWriter out) throws ResourceException {
        this.logWriter = out;
    }

    /**
     * Gets the log writer for this ManagedConnection instance.
     * 
     * @return the character output stream associated with this
     *         ManagedConnection instance
     * 
     * @exception ResourceException
     *                if the method fails
     */

    public PrintWriter getLogWriter() throws ResourceException {
        return logWriter;
    }

    /**
     * Gets the user name of the user associated with the ManagedConnection
     * instance.
     * 
     * @return the username for this connection
     */

    public String getUserName() {
        if (passCred != null)
            return passCred.getUserName();
        else
            return null;
    }

    /**
     * Gets the password for the user associated with the ManagedConnection
     * instance.
     * 
     * @return the password for this connection
     */

    public PasswordCredential getPasswordCredential() {
        return passCred;
    }

    /**
     * Associate connection handle with the physical connection.
     * 
     * @param javaMailCon
     *            connection handle
     */

    public void addJavaMailConnection(JavaMailConnection javaMailCon) {
        connectionSet.add(javaMailCon);
    }

    /**
     * Check validation of the physical connection.
     * 
     * @exception ResourceException
     *                if the connection has been destroyed
     */

    private void checkIfDestroyed() throws ResourceException {
        if (destroyed) {
            throw new IllegalStateException(
                    resource.getString("DESTROYED_CONNECTION"));
        }
    }

    /**
     * Removes the associated connection handle from the connections set to the
     * physical connection.
     * 
     * @param javaMailCon
     *            the connection handle
     */

    public void removeJavaMailConnection(JavaMailConnection javaMailCon) {
        connectionSet.remove(javaMailCon);
    }

    /**
     * Checks validation of the physical connection.
     * 
     * @return true if the connection has been destroyed; false otherwise
     */

    boolean isDestroyed() {
        return destroyed;
    }

    /**
     * Returns the ManagedConnectionFactory that created this instance of
     * ManagedConnection.
     * 
     * @return the ManagedConnectionFactory for this connection
     */

    public ManagedConnectionFactoryImpl getManagedConnectionFactory() {
        return this.mcf;
    }

    /**
     * Sends a connection event to the application server.
     * 
     * @param eventType
     *            the ConnectionEvent type
     * @param ex
     *            exception indicating a connection-related error
     */

    public void sendEvent(int eventType, Exception ex) {
        eventListener.sendEvent(eventType, ex, null);
    }

    /**
     * Sends a connection event to the application server.
     * 
     * @param eventType
     *            the ConnectionEvent type
     * @param ex
     *            exception indicating a connection-related error
     * @param connectionHandle
     *            the connection handle associated with the ManagedConnection
     *            instance
     */

    public void sendEvent(int eventType, Exception ex, Object connectionHandle) {
        eventListener.sendEvent(eventType, ex, connectionHandle);
    }

    public boolean isTheSameStore(ConnectionRequestInfoImpl cxRequestInfo) {
        logger.fine(" X.- (" + myId + ") ManagedConnection::isTheSame called");
        return store.isTheSameStore(cxRequestInfo);
    }

    /** Physical connection **/

    private boolean openStore(ConnectionRequestInfo cxRequestInfo)
            throws ResourceException {
        ConnectionSpecImpl connectionInfo = new ConnectionSpecImpl();

        if (cxRequestInfo != null) {
            connectionInfo.setUserName(((ConnectionRequestInfoImpl) cxRequestInfo).getUserName());
            connectionInfo.setPassword(((ConnectionRequestInfoImpl) cxRequestInfo).getPassword());
            connectionInfo.setServerName(((ConnectionRequestInfoImpl) cxRequestInfo).getServerName());
            connectionInfo.setProtocol(((ConnectionRequestInfoImpl) cxRequestInfo).getProtocol());
        } else {
            // Use default values
            connectionInfo.setUserName(mcf.getUserName());
            connectionInfo.setPassword(mcf.getPassword());
            connectionInfo.setServerName(mcf.getServerName());
            connectionInfo.setProtocol(mcf.getProtocol());
        }

        try {
            store = new MailServerStore(connectionInfo);
        } catch (Exception e) {
            logger.severe("JavaMailConnectionImpl::openStore threw exception: "
                    + e);
            throw new ResourceException(e.getMessage());
        }
        return true;
    }

    private jakarta.mail.Folder getFolder(String folderName)
            throws ResourceException {
        jakarta.mail.Folder folder;

        try {
            folder = store.getFolder(folderName);
            return folder;
        } catch (Exception e) {
            logger.severe("JavaMailConnectionImpl::getFolder threw exception: "
                    + e);
        }
        return null;

    }

    /**
     * Application-specific method. Fetches new messages from the mail server.
     * 
     * @return an array of messages
     */

    public jakarta.mail.Message[] getNewMessages(jakarta.mail.Folder folder)
            throws ResourceException {
        try {
            return store.getNewMessages(folder);
        } catch (Exception e) {
            logger.warning("ManagedConnectionImpl::getNewMessages threw exception: "
                    + e);
            throw new ResourceException(e.getMessage());
        }
    }

    /**
     * Application-specific method. Fetches new message headers from the mail
     * server.
     * 
     * @return a String array of message headers
     */
    public String[] getNewMessageHeaders(jakarta.mail.Folder folder)
            throws ResourceException {
        try {
            return store.getNewMessageHeaders(folder);
        } catch (Exception e) {
            logger.warning("ManagedConnectionImpl::getNewMessageHeaders threw exception: "
                    + e);
            throw new ResourceException(e.getMessage());
        }
    }
}
