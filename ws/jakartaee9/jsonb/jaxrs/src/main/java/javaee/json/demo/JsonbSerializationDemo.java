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
import javaee.json.demo.model.person.PhoneNumber;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonPatch;
import jakarta.json.JsonReader;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates JSON-B serialization
 * <p>
 * 1. Using of default mapping
 * 2. Using customizations
 * 3. Using formatting
 * 4. Using JsonbProperty and JsonbNillable annotations
 *
 * @author Dmitry Kornilov
 */
@Path("/serialization")
public class JsonbSerializationDemo {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String doGet() {
        List<Person> jasons = new ArrayList<>(3);

        List<PhoneNumber> jasonBournePhones = new ArrayList<>(2);
        jasonBournePhones.add(new PhoneNumber("home", "123 456 789"));
        jasonBournePhones.add(new PhoneNumber("work", "123 555 555"));

        jasons.add(new Person("Jason Bourne", "Super Agent", jasonBournePhones));

        List<PhoneNumber> jasonVoorheesPhones = new ArrayList<>(1);
        jasonVoorheesPhones.add(new PhoneNumber("home", "666 666 666"));

        jasons.add(new Person("Jason Voorhees", "Maniac Killer", jasonVoorheesPhones));
        jasons.add(new Person("Jason", "Argonauts Leader", null));

        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        Jsonb jsonb = JsonbBuilder.create(config);
        return jsonb.toJson(jasons);
    }

}
