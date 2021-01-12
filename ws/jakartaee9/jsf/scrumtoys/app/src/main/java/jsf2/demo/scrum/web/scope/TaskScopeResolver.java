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

package jsf2.demo.scrum.web.scope;

import java.beans.FeatureDescriptor;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.el.PropertyNotFoundException;
import jakarta.faces.context.FacesContext;

/**
 * Resolver to #{taskScope} expression.
 * @author eder
 */
public class TaskScopeResolver extends ELResolver {

    private static final String SCOPE_NAME = "taskScope";

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        if (base != null) {
            return null;
        }
        return String.class;
    }

    @Override
    public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
        return Collections.<FeatureDescriptor>emptyList().iterator();
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return Object.class;
    }

    @Override
    public Object getValue(ELContext context, Object scope, Object property) {
        if (property == null) {
            throw new PropertyNotFoundException();
        }
        if (scope == null && SCOPE_NAME.equals(property.toString())) {
            TaskScope scopeManager = getScope(context);
            context.setPropertyResolved(true);
            return scopeManager;
        } else if (scope != null && scope instanceof TaskScope) {
            //looking for bean in scope already created.
            return lookupBean(context, (TaskScope) scope, property.toString());
        } else if (scope == null) {
            return lookupBean(context, getScope(context), property.toString());
        }
        return null;
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
    }

    private TaskScope getScope(ELContext context) {
        //looking for custom scope in the session
        //if doesn't exists create and put it in the session
        FacesContext facesContext = (FacesContext) context.getContext(FacesContext.class);
        Map<String,Object> sessionMap = facesContext.getExternalContext().getSessionMap();

        TaskScope scopeManager = (TaskScope) sessionMap.get(SCOPE_NAME);
        if (scopeManager == null) {
            scopeManager = new TaskScope(facesContext.getApplication());
            sessionMap.put(SCOPE_NAME, scopeManager);
            scopeManager.notifyCreate(SCOPE_NAME,facesContext);
        }
        return scopeManager;
    }

    private Object lookupBean(ELContext context, TaskScope scope, String key) {
        //looking for mbean in taskScope
        Object value = scope.get(key);
        context.setPropertyResolved(value != null);
        return value;
    }

    public static void destroyScope() {
        //remove scope from the session
        FacesContext ctx = FacesContext.getCurrentInstance();
        Map<String,Object> sessionMap = ctx.getExternalContext().getSessionMap();
        TaskScope taskScope = (TaskScope) sessionMap.remove(SCOPE_NAME);
        if (taskScope != null)
            taskScope.notifyDestroy(SCOPE_NAME, ctx);
    }

}
