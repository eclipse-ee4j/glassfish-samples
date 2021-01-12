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

package jsf2.demo.scrum.web.controller;
import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.flow.Flow;
import jakarta.inject.Named;
@Named
@SessionScoped
public class FeatureManager implements Serializable {
    
    private static final long serialVersionUID = 1440934355780977305L;
    
    public String getFlowEnabled(String flowId) {
        FacesContext context = FacesContext.getCurrentInstance();
        String result = null;
        Flow flow = context.getApplication().getFlowHandler().getFlow(context, flowId, flowId);
        if (null != flow) {
            result = flow.getId();
        }
        return result;
        
    }
    
}
