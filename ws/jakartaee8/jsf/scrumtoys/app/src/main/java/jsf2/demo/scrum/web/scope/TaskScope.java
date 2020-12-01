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

package jsf2.demo.scrum.web.scope;

import java.util.concurrent.ConcurrentHashMap;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;

/**
 * Actually, custom scope is a Map where the instances of managed bean are store.
 */
public class TaskScope extends ConcurrentHashMap<String,Object> {

    private Application application;
    private static final long serialVersionUID = -7242422398841995164L;


    public TaskScope(Application application) {
        this.application = application;   
    }

    //will call the postConstruct method.
    public void notifyCreate(String scopeName, FacesContext facesContext) {
        ScopeContext scopeContext = new ScopeContext(scopeName, this);
        application.publishEvent(facesContext, PostConstructCustomScopeEvent.class, scopeContext);
    }

    //will call the preDestroy method.
    public void notifyDestroy(String scopeName, FacesContext facesContext) {
        ScopeContext scopeContext = new ScopeContext(scopeName, this);
        application.publishEvent(facesContext, PreDestroyCustomScopeEvent.class, scopeContext);
    }

}
