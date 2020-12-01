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

package javaee.json.demo;

import javaee.json.demo.model.person.Person;

import jakarta.json.*;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Demonstrates JSON-B deserialization and how JSON-P and JSON-B can be used together.
 * <p>
 * 1. Demonstrate how JSON-P and JSON-B can be used together
 * 2. Read Json document using JSON-P
 * 3. Use Json Pointer to get an object
 * 4. Use JSON-B to deserialize this object to POJO
 *
 * @author Dmitry Kornilov
 */
@Path("/deserialization")
public class JsonbDeserializationDemo {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person doGet() {
        JsonReader reader = Json.createReader(JsonbDeserializationDemo.class.getResourceAsStream("/jasons.json"));
        JsonArray jasons = reader.readArray();

        JsonPointer p = Json.createPointer("/1");
        JsonValue voorhees = p.getValue(jasons);

        Jsonb jsonb = JsonbBuilder.create();
        Person person = jsonb.fromJson(voorhees.toString(), Person.class);

        return person;
    }

}
