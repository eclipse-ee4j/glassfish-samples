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

package org.glassfish.samples.rest.sse.resources;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;


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

