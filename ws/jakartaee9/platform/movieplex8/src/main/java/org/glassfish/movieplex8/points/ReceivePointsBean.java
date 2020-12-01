/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.movieplex8.points;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;

/**
 * @author Arun Gupta
 */
@JMSDestinationDefinition(name = "java:global/jms/pointsQueue",
        interfaceName = "jakarta.jms.Queue")
@Named
@SessionScoped
public class ReceivePointsBean implements Serializable {

    @Inject
//    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    JMSContext context;
    @Resource(mappedName = "java:global/jms/pointsQueue")
    Queue pointsQueue;

    public String receiveMessage() {
        String message = context.createConsumer(pointsQueue).receiveBody(String.class);
        System.out.println("Received message: " + message);
        return message;
    }

    public int getQueueSize() {
        int count = 0;
        try {
            QueueBrowser browser = context.createBrowser(pointsQueue);
            Enumeration elems = browser.getEnumeration();
            while (elems.hasMoreElements()) {
                elems.nextElement();
                count++;
            }
        } catch (JMSException ex) {
            Logger.getLogger(ReceivePointsBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Getting queue size: " + count);
        return count;
    }
}
