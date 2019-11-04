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

package enterprise.rest.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import java.net.URI;
import javax.ws.rs.core.Response.Status;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageBoardTest {

    private URI serverUri;
    private Client c;
    private WebResource baseWebResource;
    
    public MessageBoardTest() {
        try {
            if(System.getProperty("samples.javaee.serveruri") != null) {
                serverUri = new URI(System.getProperty("samples.javaee.serveruri"));
            } else {
                serverUri = new URI("http://localhost:8080");
            }
            c = new Client();
            baseWebResource = c.resource(serverUri).path("message-board-war");

        } catch(Exception e) {
            assertTrue(false);
        }
    }

    @Test public void testDeployed() {
        String s = baseWebResource.get(String.class);
        assertFalse(s.length() == 0);
    }

    @Test public void testAddMessage() {
        ClientResponse response = baseWebResource.path("app/messages").post(ClientResponse.class, "hello world!");

        assertTrue(response.getResponseStatus() == Status.CREATED);

        c.resource(response.getLocation()).delete();
    }

    @Test public void testDeleteMessage() {
        URI u = baseWebResource.getURI(); // just placeholder

        ClientResponse response = baseWebResource.path("app/messages").post(ClientResponse.class, "toDelete");
        if(response.getResponseStatus() == Status.CREATED) {
            u = response.getLocation();
        } else {
            assertTrue(false);
        }

        String s = c.resource(u).get(String.class);

        assertTrue(s.contains("toDelete"));

        c.resource(u).delete();

        boolean caught = false;

        try {
            s = c.resource(u).get(String.class);
        } catch (UniformInterfaceException e) {
            if (e.getResponse().getStatus() == 404) {
                caught = true;
            }
        }

        assertTrue(caught);
    }
}

