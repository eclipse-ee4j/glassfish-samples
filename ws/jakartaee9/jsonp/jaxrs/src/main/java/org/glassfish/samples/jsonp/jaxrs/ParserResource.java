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

package org.glassfish.samples.jsonp.jaxrs;

import jakarta.json.Json;
import jakarta.json.stream.JsonParser;
import jakarta.json.stream.JsonParser.Event;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.StreamingOutput;
import java.io.*;
import java.net.URL;

/**
 * Filters JSON from twitter search REST API
 * @author Jitendra Kotamraju
 */
@Path("/parser")
public class ParserResource {

    @GET
    @Produces("text/plain")
    public StreamingOutput doGet() {
        return new StreamingOutput() {
            public void write(OutputStream os) throws IOException {
                writeTwitterFeed(os);
            }
        };
    }

    /**
     * Parses JSON from twitter search REST API
     *
     * ... { ... "from_user" : "xxx", ..., "text: "yyy", ... } ...
     *
     * then writes to HTTP output stream as follows:
     *
     * xxx: yyy
     * --------
     */
    private void writeTwitterFeed(OutputStream os) throws IOException {
        URL url = new URL("http://search.twitter.com/search.json?q=%23java");
        try(InputStream is = url.openStream();
            JsonParser parser = Json.createParser(is);
            PrintWriter ps = new PrintWriter(new OutputStreamWriter(os, "UTF-8"))) {

            while(parser.hasNext()) {
                Event e = parser.next();
                if (e == Event.KEY_NAME) {
                    if (parser.getString().equals("from_user")) {
                        parser.next();
                        ps.print(parser.getString());
                        ps.print(": ");
                    } else if (parser.getString().equals("text")) {
                        parser.next();
                        ps.println(parser.getString());
                        ps.println("---------");
                    }
                }
            }
        }
	}

}
