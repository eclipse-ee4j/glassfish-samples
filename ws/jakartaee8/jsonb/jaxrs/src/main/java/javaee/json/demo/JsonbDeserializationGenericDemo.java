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
package javaee.json.demo;

import javaee.json.demo.model.person.Person;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates deserialization of generic list.
 * <p>
 * 1. Deserialize jasons.json into List&lt;Person&gt;
 *
 * @author Dmitry Kornilov
 */
@Path("/generic")
public class JsonbDeserializationGenericDemo {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String doGet() {
        Jsonb jsonb = JsonbBuilder.create();
        Type type = new ArrayList<Person>() {
        }.getClass().getGenericSuperclass();

        List<Person> jasons = jsonb.fromJson(JsonbDeserializationGenericDemo.class.getResourceAsStream("/jasons.json"), type);
        return jasons.get(1).getName();
    }

}
