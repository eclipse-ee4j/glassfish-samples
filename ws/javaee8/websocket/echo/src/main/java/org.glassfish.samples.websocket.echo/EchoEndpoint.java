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
package org.glassfish.samples.websocket.echo;

import javax.websocket.OnMessage;
import javax.websocket.server.ServerEndpoint;

/**
 * Echo endpoint.
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
@ServerEndpoint("/echo")
public class EchoEndpoint {

    /**
     * Incoming message is represented as parameter and return value is going to be send back to peer.
     * </p>
     * {@link javax.websocket.Session} can be put as a parameter and then you can gain better control of whole
     * communication, like sending more than one message, process path parameters, {@link javax.websocket.Extension Extensions}
     * and sub-protocols and much more.
     *
     * @param message incoming text message.
     * @return outgoing message.
     *
     * @see OnMessage
     * @see javax.websocket.Session
     */
    @OnMessage
    public String echo(String message) {
        return message;
    }
}
