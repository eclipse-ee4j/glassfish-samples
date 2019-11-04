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

import javax.resource.NotSupportedException;
import javax.resource.spi.endpoint.*;
import javax.resource.spi.work.*;

import javax.mail.*;

import java.util.*;
import java.util.logging.*;

/**
 * 
 * @author Alejandro Murillo
 * 
 */

public class PollingThread implements Work {
    public static final Logger      logger            = Logger.getLogger("samples.connectors.mailconnector.ra.inbound");
    static ResourceBundle           resource          = java.util.ResourceBundle
                                                              .getBundle("samples.connectors.mailconnector.ra.inbound.LocalStrings");

    private boolean                 active            = false;
    protected transient WorkManager workManager;
    private transient HashMap       endpointConsumers = null;

    private static int              QUANTUM           = 30;                                                                          // 30
                                                                                                                                      // Seconds

    /**
     * Constructor.
     */

    public PollingThread(WorkManager workManager) {
        this.active = true;
        this.workManager = workManager;

        /*
         * Set up the hash tables for the use of the resource adapter. These
         * tables hold references to MessageEndpointFactory and
         * endpointConsumers. The factoryToConsumer table links the Message
         * factory id to the Consumer Id.
         */

        endpointConsumers = new HashMap(10);

        logger.info("[PollingThread::Constructor] Leaving");
    }

    /**
     * release: called by the WorkerManager
     */

    public void release() {
        logger.info("[S] Worker Manager called release for PollingThread ");
        active = false;
    }

    /**
     * run
     */

    public void run() {
        logger.info("[PT] WorkManager started polling thread ");

        // do not overuse system resources
        // setPriority(Thread.MIN_PRIORITY);

        while (active) {
            try {
                pollEndpoints();
                Thread.sleep(QUANTUM * 1000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.fine("[PT] Polling Thread Leaving");
    }

    private void pollEndpoints() {
        logger.fine("[PT] Polling endpoints entering");

        synchronized (endpointConsumers) {
            Collection consumers = endpointConsumers.entrySet();

            if (consumers != null) {
                Iterator iter = consumers.iterator();

                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    EndpointConsumer ec = (EndpointConsumer) entry.getValue();
                    try {
                        if (ec.hasNewMessages()) {
                            Message[] messages = ec.getNewMessages();
                            if (messages != null) {
                                for (Message msg : messages) {
                                    scheduleMessageDeliveryThread(ec, msg);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        logger.fine("[PT] Polling endpoints Leaving");
    }

    /**
     * @param message
     *            the message to be delivered
     */

    private void scheduleMessageDeliveryThread(EndpointConsumer ec, Message msg)
            throws Exception {
        logger.info("[PT] scheduling a delivery FROM: " + ec.getUniqueKey());
        try {
            Work deliveryThread = new DeliveryThread(ec, msg);
            workManager.scheduleWork(deliveryThread);
        } catch (WorkRejectedException ex) {
            NotSupportedException newEx = new NotSupportedException(
                    java.text.MessageFormat.format(
                            resource.getString("resourceadapterimpl.worker_activation_rejected"),
                            new Object[] { ex.getMessage() }));
            newEx.initCause(ex);
            throw newEx;
        } catch (Exception ex) {
            NotSupportedException newEx = new NotSupportedException(
                    java.text.MessageFormat.format(
                            resource.getString("resourceadapterimpl.worker_activation_failed"),
                            new Object[] { ex.getMessage() }));

            newEx.initCause(ex);
            throw newEx;
        }
    }

    public void stopPolling() {
        removeAllEndpointConsumers();
        this.active = false;
    }

    public void addEndpointConsumer(MessageEndpointFactory endpointFactory,
            EndpointConsumer ec) {
        logger.finest("[PT.addEndpointConsumer()] Entered");

        synchronized (endpointConsumers) {
            endpointConsumers.put(endpointFactory, ec);
        }
    }

    public void removeEndpointConsumer(MessageEndpointFactory endpointFactory) {
        logger.finest("[PT.removeEndpointConsumer()] Entered");

        EndpointConsumer ec = (EndpointConsumer) endpointConsumers
                .get(endpointFactory);

        synchronized (endpointConsumers) {
            endpointConsumers.remove(ec);
        }
    }

    /**
     * Iterates through the endpointConsumers, shutting them down and preparing
     * for stopping the Resource Adapter.
     */

    private void removeAllEndpointConsumers() {
        synchronized (endpointConsumers) {
            Collection consumers = endpointConsumers.entrySet();

            if (consumers != null) {
                Iterator iter = consumers.iterator();

                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    EndpointConsumer ec = (EndpointConsumer) entry.getValue();
                    try {
                        endpointConsumers.remove(ec);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        endpointConsumers = null;
    }
}
