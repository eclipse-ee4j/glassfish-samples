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

package org.glassfish.samples.jsonp.jaxrs;

import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Jitendra Kotamraju
 */
@Path("/structure")
public class StructureResource {
    private static final JsonBuilderFactory bf = Json.createBuilderFactory(null);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonStructure doGet() {
        return bf.createObjectBuilder()
            .add("firstName", "John")
            .add("lastName", "Smith")
            .add("age", 25)
            .add("address", bf.createObjectBuilder()
                .add("streetAddress", "21 2nd Street")
                .add("city", "New York")
                .add("state", "NY")
                .add("postalCode", "10021"))
            .add("phoneNumber", bf.createArrayBuilder()
                .add(bf.createObjectBuilder()
                    .add("type", "home")
                    .add("number", "212 555-1234"))
                .add(bf.createObjectBuilder()
                    .add("type", "fax")
                    .add("number", "646 555-4567")))
            .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void doPost(JsonStructure structure) {
        System.out.println(structure);
    }

}
