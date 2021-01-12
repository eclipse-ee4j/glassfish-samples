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

package org.glassfish.webcomm.ws;

import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.webcomm.WebCommunicationClientImpl;
import org.glassfish.webcomm.WebCommunicationProviderImpl;
import org.glassfish.grizzly.websockets.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.glassfish.webcomm.api.MessageProcessor;
import org.glassfish.webcomm.api.WebCommunicationClient;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebSocketHandler;
import org.glassfish.webcomm.annotation.WebHandler;

/**
 * WebSocketApplicationImpl class.
 * @author Santiago.PericasGeertsen@oracle.com
 */
public class WebSocketApplicationImpl extends WebSocketApplication {

    private WebCommunicationProviderImpl provider;

    private Class<? extends WebSocketHandler> clazz;

    private Map<WebSocket, WebSocketHandler> socketToHandler;

    private Map<WebSocketHandler, WebSocket> handlerToSocket;

    private Map<String, WebCommunicationContext> contexts;

    private String path;

    private Class messageClass;

    public WebSocketApplicationImpl(WebCommunicationProviderImpl provider,
        Class<? extends WebSocketHandler> clazz,
        Map<String, WebCommunicationContext> contexts, String path)
    {
        this.provider = provider;
        this.clazz = clazz;
        this.contexts = contexts;
        this.path = path;
        socketToHandler = new ConcurrentHashMap<WebSocket, WebSocketHandler>();
        handlerToSocket = new ConcurrentHashMap<WebSocketHandler, WebSocket>();
    }

    public void closeHandler(WebSocketHandler wsh) {
        WebSocket ws = handlerToSocket.get(wsh);
        assert ws != null;
        ws.close();         // Calls onClose()
    }


    /*@Override
    public WebSocket createSocket(ProtocolHandler handler,WebSocketListener... listeners) throws IOException {
        WebSocket ws = new WebSocketImpl(listeners);
        try {
            // Create instance using CDI or by hand
            WebSocketHandler wsh = provider.createHandler(clazz, contexts);

            // Update context with new instance
            WebCommunicationContext wcc = contexts.get(path);
            wcc.getHandlers().add(wsh);

            // Update internal mappings
            socketToHandler.put(ws, wsh);
            handlerToSocket.put(wsh, ws);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ws;
    }*/


    @Override
    public WebSocket createSocket(ProtocolHandler protocolHandler,WebSocketListener... listeners) {
        WebSocket ws = new WebSocketImpl(protocolHandler,listeners);
        try {
            // Create instance using CDI or by hand
            WebSocketHandler wsh = provider.createHandler(clazz, contexts);

            // Update context with new instance
            WebCommunicationContext wcc = contexts.get(path);
            wcc.getHandlers().add(wsh);

            // Update internal mappings
            socketToHandler.put(ws, wsh);
            handlerToSocket.put(wsh, ws);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ws;
    }


    @Override
    public void onConnect(WebSocket ws) {
        WebSocketHandler wsh = socketToHandler.get(ws);
        assert wsh != null;
        // TODO: Populate WebCommunicationClient instance
        WebCommunicationClient dummy = new WebCommunicationClientImpl();
        wsh.onConnect(dummy);   // Handler callback
    }

    @Override
    public void onClose(WebSocket ws, DataFrame df) {
        // Get associated handler
        WebSocketHandler wsh = socketToHandler.get(ws);
        assert wsh != null;
        wsh.onClose();          // Handler callback

        // Release handler
        provider.releaseHandler(wsh);
        
        // Update context by removing handler
        WebCommunicationContext wcc = contexts.get(path);
        wcc.getHandlers().remove(wsh);

        // Update internal mappings
        handlerToSocket.remove(wsh);
        socketToHandler.remove(ws);
    }

    @Override
    public void onMessage(WebSocket socket, String data) {
        WebSocketHandler wsh = socketToHandler.get(socket);
        assert wsh != null;

        // Determine message class from annotation, if not done already
        if (messageClass == null) {
            WebHandler wh = clazz.getAnnotation(WebHandler.class);
            messageClass = wh.messageClass();
        }

        if (messageClass == String.class) {
            try {
                wsh.onMessage(data);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        } else {
            throw new UnsupportedOperationException("TODO");
            /*
            MessageProcessor messageProcessor = provider.getMessageProcessor(
                    messageClass);
            if (messageProcessor == null) {
                throw new RuntimeException("Unable to find message processor "
                        + "for class " + messageClass);
            }
            Object obj = messageProcessor.read(new StringReader(data));
            wsh.onMessage(obj);    // Handler callback
            */
        }
    }

    @Override
    public void onMessage(WebSocket socket, byte[] data) {
        throw new UnsupportedOperationException("TODO");
        /*
        final String data = frame.getTextPayload();
        WebSocketHandler wsh = socketToHandler.get(socket);
        assert wsh != null;

        // Determine message class from annotation, if not done already
        if (messageClass == null) {
            WebHandler wh = clazz.getAnnotation(WebHandler.class);
            messageClass = wh.messageClass();
        }

        if (messageClass == String.class) {
            wsh.onMessage(data);
        } else {
            MessageProcessor messageProcessor = provider.getMessageProcessor(
                    messageClass);
            if (messageProcessor == null) {
                throw new RuntimeException("Unable to find message processor "
                        + "for class " + messageClass);
            }
            Object obj = messageProcessor.read(new StringReader(data));
            wsh.onMessage(obj);    // Handler callback
        }
        */
    }

    public void sendMessage(WebSocketHandler wsh, String data) throws IOException {
        WebSocket ws = handlerToSocket.get(wsh);
        assert ws != null;
        ws.send(data);
    }


    @Override
    public boolean isApplicationRequest(HttpRequestPacket rqst) {
        // Include context path in matching, since two applications may have
        // the handlers at the same url-pattern
        String fullPath = provider.getServletContext().getContextPath()+path;
        return rqst.getRequestURI().equals(fullPath);
    }


}
