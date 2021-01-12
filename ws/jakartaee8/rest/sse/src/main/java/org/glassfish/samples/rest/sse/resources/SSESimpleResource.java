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

package org.glassfish.samples.rest.sse.resources;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;


@Path("/")
public class SSESimpleResource {

    @Resource
    private ManagedExecutorService executor;

    @GET
    @Path("eventStream")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void eventStream(
            @Context SseEventSink eventSink,
            @Context Sse sse) {
        executor.execute(() -> {
            try (SseEventSink sink = eventSink) {
                eventSink.send(sse.newEventBuilder().data(String.class, "event1").build());
                eventSink.send(sse.newEventBuilder().data(String.class, "event2").build());
                eventSink.send(sse.newEventBuilder().data(String.class, "event3").build());
            }
            catch (Throwable e) {
                e.printStackTrace(System.out);
            }
        });

    }
}

