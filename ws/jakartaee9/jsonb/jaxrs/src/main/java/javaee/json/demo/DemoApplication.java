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

import jakarta.json.stream.JsonGenerator;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A JAX-RS Demo Application using JSON API
 *
 * @author David Kral
 */
@ApplicationPath("/")
public class DemoApplication extends Application {

    public Set<Class<?>> getClasses() {
        Set<Class<?>> set = new HashSet<>();
        set.add(JsonbSerializationDemo.class);
        set.add(JsonbDeserializationDemo.class);
        set.add(JsonbDeserializationGenericDemo.class);

        return set;
    }

    @Override
    public Map<String, Object> getProperties() {
        return new HashMap<String, Object>() {{ put(JsonGenerator.PRETTY_PRINTING, true); }};
    }
}
