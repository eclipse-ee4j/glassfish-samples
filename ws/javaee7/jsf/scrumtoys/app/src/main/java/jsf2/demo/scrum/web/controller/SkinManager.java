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

package jsf2.demo.scrum.web.controller;

import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
@Named("skinManager")
@SessionScoped
public class SkinManager extends AbstractManager implements Serializable {

    private String selectedSkin;
    @Inject
    private SkinValuesManager skinValuesManager;
    private static final long serialVersionUID = 2936693632616580209L;

    @PostConstruct
    public void construct() {
        selectedSkin = skinValuesManager.getDefaultSkinCss();
    }

    @PreDestroy
    public void destroy() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("skinManager");
                }
            }
        }
    }

    public String getSelectedSkin() {
        return selectedSkin;
    }

    public void setSelectedSkin(String selectedSkin) {
        this.selectedSkin = selectedSkin;
    }

    public SkinValuesManager getSkinValuesManager() {
        return skinValuesManager;
    }

    public void setSkinValuesManager(SkinValuesManager skinValuesManager) {
        this.skinValuesManager = skinValuesManager;
    }
    
}
