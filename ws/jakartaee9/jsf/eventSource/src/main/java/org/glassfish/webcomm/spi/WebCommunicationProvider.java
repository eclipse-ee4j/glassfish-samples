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

package org.glassfish.webcomm.spi;

import java.io.IOException;
import java.util.Map;
import jakarta.enterprise.inject.spi.BeanManager;
import org.glassfish.webcomm.api.MessageProcessor;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebSocketHandler;

/**
 * WebCommunicatinProvider class.
 * @author Santiago.PericasGeertsen@oracle.com
 * @author Roger.Kitain@oracle.com
 */
public abstract class WebCommunicationProvider {

    private BeanManager beanManager;

    public BeanManager getBeanManager() {
        return beanManager;
    }

    public void setBeanManager(BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    public abstract WebCommunicationContext createWebCommunicationContext(
            String path);

    public abstract void registerWSHandlerClass(
            Class<? extends WebSocketHandler> clazz,
            Map<String, WebCommunicationContext> contexts,
            String path);

    public abstract void registerSSEHandlerClass(
            Class<? extends ServerSentEventHandler> clazz,
            Map<String, WebCommunicationContext> contexts,
            String path);

    public abstract void sendWSMessage(WebSocketHandler wsh, Object obj)
            throws IOException;

    public abstract void sendSSEMessage(ServerSentEventHandler wsh, Object obj)
            throws IOException;

    public abstract void sendSSEMessage(ServerSentEventHandler wsh, Object obj, String eventName)
            throws IOException;

    public abstract void closeWSHandler(WebSocketHandler wsh);

    public abstract void closeSSEHandler(ServerSentEventHandler sseh);

    public abstract void registerMessageProcessor(Class clazz,
            Class<? extends MessageProcessor> processor);
    
}
