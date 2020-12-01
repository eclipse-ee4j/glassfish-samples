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

package org.glassfishsamples.ee9additions;

import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.annotation.ApplicationMap;
import jakarta.faces.annotation.HeaderMap;
import jakarta.faces.annotation.HeaderValuesMap;
import jakarta.faces.annotation.InitParameterMap;
import jakarta.faces.annotation.RequestCookieMap;
import jakarta.faces.annotation.RequestMap;
import jakarta.faces.annotation.RequestParameterMap;
import jakarta.faces.annotation.RequestParameterValuesMap;
import jakarta.faces.annotation.SessionMap;
import jakarta.faces.annotation.ViewMap;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("injectBean")
@RequestScoped
public class InjectBean {

    @Inject
    @ApplicationMap
    Map<String, Object> applicationMap;

    @Inject
    ExternalContext externalContext;

    @Inject
    FacesContext facesContext;

    @Inject
    @HeaderMap
    Map<String, String> headerMap;

    @Inject
    @HeaderValuesMap
    Map<String, String[]> headerValuesMap;

    @Inject
    @InitParameterMap
    Map<String, String> initParameterMap;

    @Inject
    @RequestCookieMap
    Map<String, Object> requestCookieMap;

    @Inject
    @RequestMap
    Map<String, Object> requestMap;

    @Inject
    @RequestParameterMap
    Map<String, String> requestParameterMap;

    @Inject
    @RequestParameterValuesMap
    Map<String, String[]> requestParameterValuesMap;
    
    @Inject
    @SessionMap
    Map<String, Object> sessionMap;
    
    @Inject
    UIViewRoot view;
    
    @Inject
    @ViewMap
    Map<String, Object> viewMap;

    Map<String, Object> injectedArtifacts;

    @PostConstruct
    public void initialize() {
        injectedArtifacts = new HashMap<>();
        injectedArtifacts.put("applicationMap", applicationMap);
        injectedArtifacts.put("externalContext", externalContext);
        injectedArtifacts.put("facesContext", facesContext);
        injectedArtifacts.put("headerMap", headerMap);
        injectedArtifacts.put("headerValuesMap", headerValuesMap);
        injectedArtifacts.put("initParameterMap", initParameterMap);
        injectedArtifacts.put("requestCookieMap", requestCookieMap);
        injectedArtifacts.put("requestMap", requestMap);
        injectedArtifacts.put("requestParameterMap", requestParameterMap);
        injectedArtifacts.put("requestParameterValuesMap", requestParameterValuesMap);
        injectedArtifacts.put("sessionMap", sessionMap);
        injectedArtifacts.put("view", view);
        injectedArtifacts.put("viewMap", viewMap);
    }

    public Map<String, Object> getInjectedArtifacts() {
        return injectedArtifacts;
    }
}
