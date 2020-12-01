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
import jakarta.annotation.Resource;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * @author Arun Gupta
 */
@Named
@SessionScoped
public class SendPointsBean implements Serializable {

    @Inject
//    @JMSConnectionFactory("java:comp/DefaultJMSConnectionFactory")
    JMSContext context;
    
    @NotNull
    @Pattern(regexp = "^\\d{2},\\d{2}", message = "Message format must be 2 digits, comma, 2 digits, e.g. 12,12")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Resource(mappedName = "java:global/jms/pointsQueue")
    Queue pointsQueue;

    public void sendMessage() {
        System.out.println("Sending message: " + message);

        context.createProducer().send(pointsQueue, message);
    }
}
