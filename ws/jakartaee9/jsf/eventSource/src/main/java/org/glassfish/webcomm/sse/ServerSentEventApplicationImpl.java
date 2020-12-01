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

package org.glassfish.webcomm.sse;

import org.glassfish.webcomm.WebCommunicationClientImpl;
import org.glassfish.webcomm.WebCommunicationInjector;
import org.glassfish.webcomm.WebCommunicationProviderImpl;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;

/**
 * ServerSentEventApplicationImpl class.
 * @author Santiago.PericasGeertsen@oracle.com
 * @author Roger.Kitain@oracle.com
 */
public class ServerSentEventApplicationImpl implements AsyncListener {

    private WebCommunicationProviderImpl provider;

    private Class<? extends ServerSentEventHandler> clazz;

    private Map<String, WebCommunicationContext> contexts;

    private Map<ServerSentEventHandler, AsyncContext> handlerToContext;

    private Map<AsyncContext, ServerSentEventHandler> contextToHandler;

    private String path;

    public ServerSentEventApplicationImpl(WebCommunicationProviderImpl provider,
        Class<? extends ServerSentEventHandler> clazz,
        Map<String, WebCommunicationContext> contexts, String path)
    {
        this.provider = provider;
        this.clazz = clazz;
        this.contexts = contexts;
        this.path = path;
        handlerToContext = new ConcurrentHashMap<ServerSentEventHandler, AsyncContext>();
        contextToHandler = new ConcurrentHashMap<AsyncContext, ServerSentEventHandler>();
    }

    public String getPath() {
        return path;
    }

    public void createConnection(AsyncContext asyncContext) {
        try {
            // Create instance using CDI or by hand
            ServerSentEventHandler sseh = provider.createHandler(clazz, contexts);

            // Update context with new instance
            WebCommunicationContext wcc = contexts.get(path);
            wcc.getHandlers().add(sseh);

            // Update internal mapping
            handlerToContext.put(sseh, asyncContext);
            contextToHandler.put(asyncContext, sseh);

            // Call onConnect on handler
            onConnect(sseh);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void onConnect(ServerSentEventHandler sseh) {
        AsyncContext ac = handlerToContext.get(sseh);
        WebCommunicationClientImpl client = new WebCommunicationClientImpl();
        final ServletRequest r = ac.getRequest();
        client.setRemoteAddr(r.getRemoteAddr());
        client.setRemoteHost(r.getRemoteHost());
        client.setRemotePort(r.getRemotePort());
        client.setRemoteUser(((HttpServletRequest)r).getRemoteUser());
        client.setUserPricipal(((HttpServletRequest)r).getUserPrincipal());
        HttpServletRequest httpReq = (HttpServletRequest) ac.getRequest();
        client.setHttpSession(httpReq.getSession());
        sseh.onConnect(client);     // Handler callback
    }

    private static final byte[] dataKey;
    static {
        try {
            dataKey = "data:".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static final byte[] eventKey;
    static {
        try {
            eventKey = "event:".getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(ServerSentEventHandler sseh, String data)
            throws IOException
    {
        synchronized (sseh) {
            AsyncContext ac = handlerToContext.get(sseh);
            if (ac == null) {
                return;         // removed
            }

            try {
                // Write message on response and flush
                HttpServletResponse res = (HttpServletResponse) ac.getResponse();
                ServletOutputStream sos = res.getOutputStream();
                sos.write(dataKey);
                sos.write(data.getBytes("UTF-8"));
                sos.write('\n');
                sos.write('\n');
                sos.flush();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } 
        }
    }

    public void sendMessage(ServerSentEventHandler sseh, String data, String eventName)
            throws IOException
    {
        synchronized (sseh) {
            AsyncContext ac = handlerToContext.get(sseh);
            if (ac == null) {
                return;         // removed
            }

            try {
                // Write message on response and flush
                HttpServletResponse res = (HttpServletResponse) ac.getResponse();
                ServletOutputStream sos = res.getOutputStream();
                sos.write(eventKey);
                sos.write(eventName.getBytes("UTF-8"));
                sos.write('\n');
                sos.write(dataKey);
                sos.write(data.getBytes("UTF-8"));
                sos.write('\n');
                sos.write('\n');
                sos.flush();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void closeHandler(ServerSentEventHandler sseh) {
        synchronized (sseh) {
            AsyncContext ac = handlerToContext.get(sseh);
            if (ac == null) {
                return;         // removed
            }
            ac.complete();      // calls onComplete()
        }
    }

    // -- AsyncListener --------------------------------------------------

    public void onComplete(AsyncEvent event) throws IOException {
        // Call onClose() on handler
        AsyncContext ac = event.getAsyncContext();
        ServerSentEventHandler sseh = contextToHandler.get(ac);
        sseh.onClose();         // Handler callback

        // Remove handler from context
        WebCommunicationContext wcc = contexts.get(path);
        wcc.getHandlers().remove(sseh);

        // Update internal mappings
        handlerToContext.remove(sseh);
        contextToHandler.remove(ac);

        // Release handler
        provider.releaseHandler(sseh);
    }

    public void onTimeout(AsyncEvent event) throws IOException {
        // no-op
    }

    public void onError(AsyncEvent event) throws IOException {
        // no-op
    }

    public void onStartAsync(AsyncEvent event) throws IOException {
        // no-op
    }

}
