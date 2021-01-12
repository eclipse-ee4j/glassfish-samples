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

package jsf2.demo.scrum.web.helper;

import jsf2.demo.scrum.model.entities.Project;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@FacesConverter(forClass = Project.class)
public class ProjectConverter implements Converter {

    private static Map<Long, Project> cache = new HashMap<Long, Project>();

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.equals("0")) {
            return null;
        }
        try {
            return cache.get(Long.parseLong(value));
        } catch (NumberFormatException e) {
            throw new ConverterException("Invalid value: " + value, e);
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object object) {
        Project project = (Project) object;
        Long id = project.getId();
        if (id != null) {
            cache.put(id, project);
            return String.valueOf(id.longValue());
        } else {
            return "0";
        }
    }
}
