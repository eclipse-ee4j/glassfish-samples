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

import jakarta.mail.*;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import samples.connectors.mailconnector.share.ConnectionSpecImpl;

/**
 * @author Alejandro Murillo
 */

public class MailServerStore {
    private Session       session;
    private Authenticator authenticator;
    private Store         store;
    private Properties mailProperties;

    private String                   userName, password, serverName, protocol;

    public static final Logger  logger = Logger.getLogger("samples.connectors.mailconnector.ra.outbound");

    /**
     * Constructor.
     * 
     * @param spec
     *            the ConnectionSpec (ConnectionRequestInfo)
     */
    public MailServerStore(ConnectionSpecImpl spec) throws Exception {
        userName = spec.getUserName();
        password = spec.getPassword();
        serverName = spec.getServerName();
        protocol = spec.getProtocol();

        this.authenticator = null;

        mailProperties = new Properties();
        mailProperties.setProperty("mail.transport.protocol", "smtp");
        mailProperties.setProperty("mail.store.protocol", protocol);
        mailProperties.setProperty("mail.smtp.host", serverName);

        connectStore();
    }

    /**
     * Closes the Store.
     * 
     * @exception Exception
     *                if the close fails
     */

    public void closeStore() throws Exception {
        /*
         * The JavaMail Session object does not have an explicit close.
         */

        // logger.info("Listener::close()");
        this.store.close();
        this.store = null;
        this.authenticator = null;
        this.session = null;
    }

    /**
     * Opens a connection to the mail server. Associated with a MC
     * 
     * @exception Exception
     *                if the open fails
     */

    private void connectStore() throws Exception {
        try {
            // Get a session object
            session = jakarta.mail.Session.getDefaultInstance(mailProperties);
            
            // Get a store object
            store = session.getStore("imap");
            this.store.connect(serverName, userName, password);
        } catch (Exception te) {
            logger.log(Level.SEVERE, "[S] Caught an exception when obtaining a JavaMail Session", te);
            throw new Exception(te.getMessage());
        }
    }

    public jakarta.mail.Folder getFolder(String folderName) throws Exception {
        jakarta.mail.Folder folder;
        folder = this.store.getFolder(folderName);

        if ((folder == null) || (!folder.exists())) {
            Exception e = new Exception("Folder " + folderName  + " does not exist or is not found");
            throw e;
        }

        return folder;
    }

    /**
     * Retrieves new messages. Used by a JavaMailConnection
     * 
     * @return an array of messages
     */

    public Message[] getNewMessages(jakarta.mail.Folder folder) throws Exception {
        if ((folder == null) || (!folder.exists())) {
            Exception e = new Exception("Folder " + folder
                    + " does not exist or is not found");
            throw e;
        }

        if (!folder.isOpen()) {
            folder.open(jakarta.mail.Folder.READ_WRITE);
        }

        //
        // Deliver only new messages to the MDB
        //
        try {
            int newMsgs = folder.getNewMessageCount();
            if (newMsgs > 0) {
                int msgCount = folder.getMessageCount();
                Message msgs[] = folder.getMessages(msgCount - newMsgs + 1, msgCount);
                return msgs;
            }
        } catch (Exception e) {
            logger.info("[S] Exception obtaining messages from mail server");
        }
        return null;
    }

    /**
     * Retrieves headers of new messages.
     * 
     * @return a string array containing the message headers
     */

    public String[] getNewMessageHeaders(jakarta.mail.Folder folder)
            throws Exception {
        if ((folder == null) || (!folder.exists())) {
            Exception e = new Exception("Folder " + folder
                    + " does not exist or is not found");
            throw e;
        }

        if (!folder.isOpen()) {
            folder.open(jakarta.mail.Folder.READ_WRITE);
        }
        //
        // Deliver only new messages to the MDB
        //

        try {
            int newMsgs = folder.getNewMessageCount();

            if (newMsgs > 0) {
                int msgCount = folder.getMessageCount();
                Message[] msgs = folder.getMessages(msgCount - newMsgs + 1,
                        msgCount);
                String[] headers = new String[msgs.length];
                logger.info("messages length: " + msgs.length);
                logger.info("headers length: " + headers.length);
                for (int i = 0; i < headers.length; i++) {
                    logger.info("<MSF> Packing message with SUBJECT: "
                            + msgs[i].getSubject());
                    headers[i] = msgs[i].getSubject();
                }
                return headers;
            }
        } catch (Exception e) {
            logger.severe("[S] Exception obtaining messages from mail server:");
            e.printStackTrace();
        }
        return null;
    }

    public boolean isTheSameStore(ConnectionRequestInfoImpl cxRequestInfo) {
        if (!userName.equals(cxRequestInfo.getUserName()))
            return false;
        if (!password.equals(cxRequestInfo.getPassword()))
            return false;
        if (!serverName.equals(cxRequestInfo.getServerName()))
            return false;
        if (!protocol.equals(cxRequestInfo.getProtocol()))
            return false;

        logger.info("isTheSameStore: found match!");
        return true;
    }
}
