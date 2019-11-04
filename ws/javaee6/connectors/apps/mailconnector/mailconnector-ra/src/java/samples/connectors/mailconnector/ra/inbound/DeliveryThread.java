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


import java.util.*;
import java.util.logging.*;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.resource.spi.work.*;

/**
 * 
 * @author Alejandro Murillo
 *
 */

public class DeliveryThread  implements Work, WorkContextProvider
{
    public static final Logger  logger = 
        Logger.getLogger("samples.connectors.mailconnector.ra.inbound");
    static ResourceBundle 	resource = 
        java.util.ResourceBundle.getBundle("samples.connectors.mailconnector.ra.inbound.LocalStrings"); 

    private EndpointConsumer  endpointConsumer;
    private Message msg;
    private List<WorkContext> workContexts = new ArrayList<WorkContext>();
    
    /**
     * Constructor.
     */

    public DeliveryThread(EndpointConsumer endpointConsumer, Message msg)
    {
        this.endpointConsumer = endpointConsumer;
        this.msg = msg;
        initializeWorkContexts(msg);
        logger.fine("[DeliveryThread::Constructor] Leaving");

    }

    /**
     * release: called by the WorkerManager
     */

    public void release()
    {
        logger.fine("[DT] Worker Manager called release for deliveryThread ");
    }

    /**
     * run
     */

    public void run()
    { 
	logger.fine("[DT] WorkManager started delivery thread ");
                
        try
	{
            endpointConsumer.deliverMessage(msg);
        } catch (Exception te) {
            logger.info("deliveryThread::run got an exception");
            te.printStackTrace();
        }
        
	logger.fine("[DT] DeliveryThread leaving");
    }

    public List<WorkContext> getWorkContexts() {
        return workContexts;
    }

    private void initializeWorkContexts(Message msg) {
        try {
            Address[] recepients = msg.getFrom();
            if(recepients != null && recepients.length > 0){
                //Let us consider first recepient alone.
                Address recepient = recepients[0];
                String recepientId = recepient.toString();

                if(recepientId.indexOf("@") > 0){
                    recepientId = recepientId.substring(0,recepientId.indexOf("@"));
                }

                //Assuming that the password is same as username
                MySecurityContext mysc = new MySecurityContext(recepientId, recepientId, recepientId);
                getWorkContexts().add(mysc);
            }
        } catch (MessagingException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

    }

}
