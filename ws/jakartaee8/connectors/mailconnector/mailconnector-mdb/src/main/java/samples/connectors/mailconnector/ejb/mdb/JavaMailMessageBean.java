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

package samples.connectors.mailconnector.ejb.mdb;

import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenBean;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDrivenContext;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import java.util.*;
import java.util.logging.*;
import java.text.*;
import javax.naming.*;
import javax.mail.*;
import javax.mail.internet.MimeMessage;

import samples.connectors.mailconnector.api.*;

/**
 * A simple message-driven bean. The code illustrates the requirements of a
 * message-driven bean class:
 * <ul>
 * <li>It implements the <code>JavaMailMessageListener</code> interface.
 * <li>The class is defined as <code>public</code>.
 * <li>The class cannot be defined as <code>abstract</code> or
 * <code>final</code>.
 * <li>It implements one <code>onMessage</code> method.
 * <li>It implements one <code>ejbCreate</code> method and one
 * <code>ejbRemove</code> method.
 * <li>It contains a public constructor with no arguments.
 * <li>It must not define the <code>finalize</code> method.
 * </ul>
 * Unlike session and entity beans, message-driven beans do not have the remote
 * or local interfaces that define client access. Client components do not
 * locate message-driven beans and invoke methods on them. Although
 * message-driven beans do not have business methods, they may contain helper
 * methods that are invoked internally by the onMessage method.
 */
@MessageDriven(messageListenerInterface=JavaMailMessageListener.class,
        name="JavaMailMDB",
        activationConfig = {
        @ActivationConfigProperty(propertyName = "serverName", propertyValue = "localhost"),
        @ActivationConfigProperty(propertyName = "userName", propertyValue = "joe"),
        @ActivationConfigProperty(propertyName = "password", propertyValue = "joe"),
        @ActivationConfigProperty(propertyName = "folderName", propertyValue = "INBOX"),
        @ActivationConfigProperty(propertyName = "protocol", propertyValue = "IMAP"),
        @ActivationConfigProperty(propertyName = "interval", propertyValue = "30") })
@TransactionManagement(TransactionManagementType.CONTAINER)

public class JavaMailMessageBean implements MessageDrivenBean,
        JavaMailMessageListener {
    static final Logger logger = Logger.getLogger("samples.connectors.mailconnector.ejb.mdb");

    private transient MessageDrivenContext mdc    = null;
    private Context context;

    /**
     * Default constructor. Creates a bean.
     */

    public JavaMailMessageBean() {
        logger.info("<MDB> In JavaMailMessageBean.JavaMailMessageBean()");
    }

    /**
     * Sets the context for the bean.
     * 
     * @param mdc
     *            the message-driven-bean context
     */

    public void setMessageDrivenContext(MessageDrivenContext mdc) {
        logger.info("<MDB> In JavaMailMessageBean.setMessageDrivenContext()");
        this.mdc = mdc;
    }

    /**
     * Creates a bean. Required by EJB spec.
     */

    public void ejbCreate() {
        logger.info("<MDB> In JavaMailMessageBean.ejbCreate()");
    }

    /**
     * When the inbox receives a message, the EJB container invokes the
     * <code>onMessage</code> method of the message-driven bean. The
     * <code>onMessage</code> method displays information from the message
     * headers, formulates a reply to the message, and sends it. (The code to
     * send the reply is currently commented out.)
     * 
     * @param message
     *            incoming message
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void onMessage(javax.mail.Message message) {
        final String mailer = "JavaMailer";

        try {
            logger.info("<MDB> ---- Got a message ");
            logger.info("<MDB> SUBJECT: " + message.getSubject());

            context = new InitialContext();
            Session session = (Session) context
                    .lookup("java:app/env/TheMailSession");

            Message reply = new MimeMessage(session);
            reply.setFrom();

            Address[] addresses = message.getFrom();
            if ((addresses != null) || (addresses.length < 1)) {
                addresses = message.getReplyTo();
            }

            logger.info("<MDB> SENDER : " + addresses[0].toString());

            Address[] recepients = message.getFrom();
            if (recepients != null && recepients.length > 0) {
                String recepientId = recepients[0].toString();
                if (recepientId.indexOf("@") > 0) {
                    recepientId = recepientId.substring(0,
                            recepientId.indexOf("@"));
                }
                // logger.info("<MDB> isCallerInRole("+recepientId+") : " +
                // mdc.isCallerInRole(recepientId));
                logger.info("<MDB> getCallerPrincipal() : "
                        + mdc.getCallerPrincipal());
            }

            reply.setRecipients(Message.RecipientType.TO, addresses);
            reply.setSubject("MDB reply RE: " + message.getSubject());

            DateFormat dateFormatter = DateFormat.getDateTimeInstance(
                    DateFormat.LONG, DateFormat.SHORT);

            Date timeStamp = new Date();

            String messageText = "Dear " + addresses[0].toString() + '\n'
                    + "Thank you for your email." + '\n'
                    + "We received your message on "
                    + dateFormatter.format(timeStamp) + ".";

            reply.setText(messageText);

            reply.setHeader("X-Mailer", mailer);
            reply.setSentDate(timeStamp);

            // We used to reply to the sender, but this might
            // send undesired replies when testing with a busy
            // mailbox. Uncomment the following line if you wish
            // to do that.

            // Transport.send(reply);

        } catch (Exception e) {
            // logger.info("<MDB> Could not send the email: " + e.toString());
            throw new EJBException(e.getMessage());
        }
    } // onMessage

    /**
     * Removes the bean. Required by EJB specification.
     */

    public void ejbRemove() {
        logger.info("<MDB> In JavaMailMessageBean.remove()");
    }

} // class
