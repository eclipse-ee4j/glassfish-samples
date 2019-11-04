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

import javax.resource.spi.endpoint.*;
import javax.resource.spi.work.*;

import java.rmi.*;
import java.util.logging.*;
import java.lang.reflect.*;

import java.util.List;
import javax.mail.*;
import javax.mail.Message.*;

//import samples.connectors.mailconnector.ra.*;
//import samples.connectors.mailconnector.backend.*;


/**
 * JavaMail Client RMI interface.
 * 
 * This is a singleton class that represents the Client interface
 * used by the JavaMail Service.
 * 
 * @author Alejandro Murillo
 */

public class EndpointConsumer
{
    ActivationSpecImpl      activationSpec;
    MessageEndpointFactory  endpointFactory;
    MailServerFolder        folder  = null;
        
        
    static Logger logger = 
        Logger.getLogger("samples.connectors.mailconnector.ra.inbound",
        	    "samples.connectors.mailconnector.ra.inbound.LocalStrings"); 

    public Method onMessage = null;

    /**
     * Constructor. Creates a JavaMail Client Interface object and exports it 
     * so that the server can access it.
     *
     * @param endpointFactory a MessageEndpointFactory
     * @param activationSpec  the activation spec
     */

    public EndpointConsumer(MessageEndpointFactory endpointFactory,
				ActivationSpecImpl activationSpec)
	throws Exception 
    {
	this.endpointFactory = endpointFactory;
	this.activationSpec  = activationSpec;
	try
	{
            folder = new MailServerFolder(activationSpec);  
	} catch(AuthenticationFailedException ie) {
	    logger.info("[EC] Authentication problem when opening Mail Folder: " + 
			getUniqueKey() + 
			" Wrong password?, fix ejb-jar.xml, rebuild and redeploy");
	    //ie.printStackTrace();
	    throw ie;
	} catch(Exception ie) {
	    logger.info("[EC] Unexpected Error while opening Mail Folder: " + 
			getUniqueKey() + 
			" check for typos with foldername, username, password or hostname in ejb-jar.xml, rebuild and redeploy");
	    //ie.printStackTrace();
	    throw ie;
	}
        logger.info("[EC] Created EndpointConsumer for: " + getUniqueKey());
    }

    /**
     * Delivers it to the appropriate EndPoint.
     *
     * @param message  the message to be delivered
     */

    public void deliverMessage(javax.mail.Message message)
	throws RemoteException
    {
        MessageEndpoint endpoint = null;

        Object[] args = { message };
        
        try
        {
           // o Create endpoint, passing XAResource.
           // o Call beforeDelivery to allow the appserver
	   //   to engage delivery in transaction, if required.
           // o Deliver Message.
           
           if ( (endpoint = endpointFactory.createEndpoint(null)) != null)
	   {
	       // If this was an XA capable RA then invoke 
	       //  endpoint.beforeDelivery();
               ((samples.connectors.mailconnector.api.JavaMailMessageListener) 
                   endpoint).onMessage(message);
	   }
        } catch(Exception e) {
            logger.log(Level.WARNING, "messagereceiver.onmessageexception", 
                e.getMessage());
        } catch(Error error) {
            logger.log(Level.WARNING, "messagereceiver.onmessageexception", 
                error.getMessage());
        } catch(Throwable t) {
            logger.log(Level.WARNING, "messagereceiver.onmessageexception", 
                t.getMessage());
        } finally {
            // o Call afterDelivery to to permit the Application Server to 
            //   complete or rollback transaction on  delivery. This should 
            //   occur even if an exception has been thrown.
            // o Call release to indicate the endpoint can be recycled.
            
            if (endpoint != null)
            {
	        //If this was an XA capable RA then invoke 
	        //  endpoint.afterDelivery();
                endpoint.release();
            }
        }
    }
                                    
    public boolean hasNewMessages()
	throws Exception
    {
    logger.fine("[EC] Checking for new messages on: " + getUniqueKey());
	return  folder.hasNewMessages();
    }

    public String getUniqueKey()
    {
        return  activationSpec.getUserName() + "::" +
		activationSpec.getFolderName() + "@" + 
		activationSpec.getServerName();
    }

    public Message[] getNewMessages(){
        Message msgs[] = null;
        try
	 {
	      	msgs = folder.getNewMessages();
	        if (msgs != null)
	        {
	            for (int i = 0; i < msgs.length; i++)
	      	    {
	                if ( !msgs[i].isSet(Flags.Flag.SEEN) ) //Deliver only once
                        {
                            //deliverMessage(msgs[i]);
		            // Mark message as seen
		            msgs[i].setFlag(Flags.Flag.SEEN, true);
                        }
	            }
	        }
	 } catch(Exception ie) {
		logger.info("[EC] getNewMessages caught an exception. Bailing out");
		ie.printStackTrace();
	 }
        return msgs;

    }
}
