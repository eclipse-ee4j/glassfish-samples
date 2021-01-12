/*
 	Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
	
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

package samples.connectors.mailconnector.ra.inbound;

import javax.mail.*;

import java.util.*;
import java.util.logging.*;

/**
 * @author Alejandro Murillo
 */

// public class MailServerFolder implements MessageCountListener
public class MailServerFolder {
    private javax.mail.Session       session;
    private javax.mail.Authenticator authenticator;
    private javax.mail.Store         store;
    private javax.mail.Folder        folder;
    private Properties               mailProperties;

    private String protocol;
    private String serverName;
    private String userName;
    private String password;
    private String folderName;

    public static final Logger  logger = Logger.getLogger("samples.connectors.mailconnector.ra.inbound");

    /**
     * Constructor.
     * 
     * @param spec
     *    the ActivationSpec for the MDB
     */

    public MailServerFolder(ActivationSpecImpl spec) throws Exception {
        folderName = spec.getFolderName();
        userName = spec.getUserName();
        password = spec.getPassword();
        serverName = spec.getServerName();
        protocol = spec.getProtocol();

        this.authenticator = null;

        mailProperties = new Properties();
        mailProperties.setProperty("mail.transport.protocol", "smtp");
        mailProperties.setProperty("mail.store.protocol", protocol);
        mailProperties.setProperty("mail.smtp.host", serverName);
//        mailProperties.setProperty("mail.imap.class", "org.jvnet.mock_javamail.MockStore");

        try {
            open();
        } catch (Exception te) {
            logger.log(Level.SEVERE,
                    "[S] Caught an exception when opening the Folder", te);
            throw te;
        }
    }

    /**
     * Closes the folder.
     * 
     * @exception Exception
     *                if the close fails
     */

    public void close() throws Exception {
        /*
         * The JavaMail Session object does not have an explicit close.
         */

        // logger.info("Listener::close()");
        this.store.close();
        this.store = null;
        this.session = null;
        this.authenticator = null;

    }

    /**
     * Opens a connection to the mail server.
     * 
     * @exception Exception
     *                if the open fails
     */

    private void open() throws Exception {
        try {
            // Get a session object
            session = javax.mail.Session.getDefaultInstance(mailProperties);
            
            // Get a store object
            store = session.getStore("imap");
        } catch (Exception te) {
            logger.log(Level.FINE, "[MSF] Caught an exception when obtaining a JavaMail Session", te);
            throw te;
        }
        
        try {
            store.connect(serverName, userName, password);
        } catch (MessagingException e) {
            logger.log(Level.SEVERE, "[MSF] Caught an exception while connecting to Mail Store, properties are: "+mailProperties, e);
            throw e;
        }
        folder = store.getFolder(folderName);

        if ((folder == null) || (!this.folder.exists())) {
            Exception e = new Exception("Folder " + folderName + " does not exist or is not found");
            throw e;
        }

        folder.open(javax.mail.Folder.READ_WRITE);
        // folder.addMessageCountListener(this); //set this as the folder
        // MessageCountListener
    }

    private void reopen() throws Exception {
        if (!folder.isOpen()) {
            // logger.info("[MSF] Reopening folder");
            folder.open(javax.mail.Folder.READ_WRITE);
            // folder.addMessageCountListener(this); //set this as the folder
            // MessageCountListener
        }
    }

    /**
     * Retrieves new messages.
     * 
     * @return an array of messages
     */

    public Message[] getNewMessages() throws Exception {
        reopen(); // Make sure the folder is open

        //
        // Deliver only new messages to the MDB
        //

        try {
            if (folder.hasNewMessages()) {
                int newMsgs = folder.getNewMessageCount();
                int msgCount = folder.getMessageCount();
                Message msgs[] = folder.getMessages(msgCount - newMsgs + 1,  msgCount);
                return msgs;
            }
        } catch (Exception e) {
            logger.info("[MSF] Exception obtaining messages from mail server");
        }
        return null;
    }

    /**
     * Retrieves headers of new messages.
     * 
     * @return a string array containing the message headers
     */

    public String[] getNewMessageHeaders() throws Exception {
        reopen(); // Make sure the folder is open

        //
        // Deliver only new messages to the MDB
        //

        try {
            if (folder.hasNewMessages()) {
                int newMsgs = folder.getNewMessageCount();
                int msgCount = folder.getMessageCount();
                Message[] msgs = folder.getMessages(msgCount - newMsgs + 1,
                        msgCount);
                String[] headers = new String[msgs.length];
                logger.info("messages length: " + msgs.length);
                logger.info("headers length: " + headers.length);
                for (int i = 0; i < headers.length; i++) {
                    logger.info("[MSF] Packing message with SUBJECT: "
                            + msgs[i].getSubject());
                    headers[i] = msgs[i].getSubject();
                }
                return headers;
            }
        } catch (Exception e) {
            logger.severe("[MSF] Exception obtaining messages from mail server:");
            e.printStackTrace();
        }
        return null;
    }

    public boolean hasNewMessages() throws Exception {
        reopen(); // Make sure the folder is open

        /****
         * logger.info("Folder <" + folder.getName() + "> has (" +
         * folder.getUnreadMessageCount() + ") unread messages");
         * logger.info("Folder <" + folder.getName() + "> has (" +
         * folder.getNewMessageCount() + ") NEW messages");
         ****/

        return folder.hasNewMessages();
    }

    /**
     * These 2 methods should be uncommented when this class implements the
     * MessageCountListener interface
     * 
     * 
     * 
     * public void messagesAdded(MessageCountEvent e) {
     * logger.info("messagesAdded: ENTERED"); try { Message[] msgs =
     * e.getMessages(); logger.info("messagesAdded: " + msgs.length); for (int i
     * = 0; i < msgs.length; i++) {
     * logger.info("<MSF> Added Message with SUBJECT: " + msgs[i].getSubject());
     * } } catch (Exception ex) {
     * logger.info("[S] Got an Exception in messagesAdded"); } }
     * 
     * public void messagesRemoved(MessageCountEvent e) {
     * logger.info("messagesRemoved: ENTERED"); try { Message[] msgs =
     * e.getMessages(); logger.info("messagesRemoved: " + msgs.length); for (int
     * i = 0; i < msgs.length; i++) {
     * logger.info("<MSF> Removed Message with SUBJECT: " +
     * msgs[i].getSubject()); } } catch (Exception ex) {
     * logger.info("[S] Got an Exception in messagesRemoved"); } }
     * 
     */

}
