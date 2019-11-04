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
package org.glassfish.samples.rest.chat.resources;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Response;

import javax.ejb.Singleton;


/**
 * Resource that handles one way asynchronous chatting based on consumes produces mechanism.
 * <p/>
 * Message is sent (produces) using POST http method handled by {@link #postMessage(javax.ws.rs.container.AsyncResponse, String)}
 * and this message is received (consumed) by GET http method handled by {@link #getMessage(javax.ws.rs.container.AsyncResponse,
 * String)}.
 * <p/>
 * This implementation of produces consumes mechanism does not keep a queue of messages but a queue of responses waiting for
 * messages. Message is always delivered directly from thread handling posting of message to the thread handling receiving of
 * message. Request processing must be therefore synchronized by blocking threads. In order to save server resources the original
 * threads are returned to the container.
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */
@Path("/")
@Singleton
public class ChatResource {

    /**
     * This queue synchronizes produces/consumes operations. It contains {@link AsyncResponse async responses} into
     * which post message should be written.
     */
    private final BlockingQueue<AsyncResponseWrapper> suspended = new ArrayBlockingQueue<AsyncResponseWrapper>(10);

    /**
     * Internal response wrapper which bundles response with id.
     */
    private static class AsyncResponseWrapper {
        private final AsyncResponse asyncResponse;
        private final String id;

        private AsyncResponseWrapper(AsyncResponse asyncResponse, String id) {
            this.asyncResponse = asyncResponse;
            this.id = id;
        }

        public AsyncResponse getAsyncResponse() {
            return asyncResponse;
        }

        public String getId() {
            return id;
        }
    }


    /**
     * Handle a HTTP get message asynchronously (suspend response in order to release the container thread instead of
     * blocking it on the blocking queue).
     *
     * @param asyncResponse Suspended asynchronous response (injected).
     * @param requestId Header identifying the header.
     */
    @GET
    public void getMessage(@Suspended final AsyncResponse asyncResponse, final @HeaderParam("request-id") String requestId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // Put actual response to the queue. This response will be later taken and resumed with
                    // the message.
                    suspended.put(new AsyncResponseWrapper(asyncResponse, requestId));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * Handle a HTTP POST method asynchronously (suspend response in order to release the container thread instead of
     * blocking it on the blocking queue).
     *
     * @param postAsyncResponse Suspended asynchronous response (injected).
     * @param message Message to be sent.
     */
    @POST
    public void postMessage(@Suspended final AsyncResponse postAsyncResponse, final String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Take one response from the queue and resume it with the message. If no message is in the queue now
                    // then this method will block the thread until the response is put into queue (by GET http method).
                    final AsyncResponseWrapper responseWrapper = suspended.take();
                    responseWrapper.getAsyncResponse().resume(Response.ok()
                            .entity(message).header("request-id", responseWrapper.getId()).build());

                    // Now resume response connected with the request invoking this post method just reply that the message
                    // was delivered.
                    postAsyncResponse.resume("Sent!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Get the string representation of internal async response queue <code>suspended<code/>.
     * <p/>
     * This resource is requested in regular intervals by clients.
     *
     * @return Plain text with list of request-id of async responses.
     */
    @GET
    @Path("queue")
    @Produces("text/html")
    public String getResponseQueue() {
        StringBuffer sb = new StringBuffer();
        boolean addSeparator = false;
        for (AsyncResponseWrapper asyncResponseWrapper : suspended) {
            if (addSeparator) {
                sb.append(", ");
            } else {
                addSeparator = true;
            }
            sb.append(asyncResponseWrapper.getId());
        }
        return sb.toString();
    }
}
