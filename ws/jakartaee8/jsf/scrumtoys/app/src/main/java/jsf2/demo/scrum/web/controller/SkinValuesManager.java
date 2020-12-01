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
import javax.annotation.PostConstruct;

import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PreDestroy;
import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;


/**
 * @author edermag
 */
@Named("skinValuesManager")
@ApplicationScoped
public class SkinValuesManager implements Serializable {

    private Map<String, String> values;
    private List<ColorTuple> displayNames;

    private final String defaultSkin = "blue";
    private static final long serialVersionUID = 2238251086172648511L;

    @PostConstruct
    public void construct() {
        values = new LinkedHashMap<String, String>();
        displayNames = new ArrayList<ColorTuple>();
        
        ViewDeclarationLanguageFactory vdlFactory = (ViewDeclarationLanguageFactory) FactoryFinder.getFactory(FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY);
        ViewDeclarationLanguage vdl = vdlFactory.getViewDeclarationLanguage("home.xhtml");
        FacesContext context = FacesContext.getCurrentInstance();
        List<String> availableContracts = vdl.calculateResourceLibraryContracts(context, "home.xhtml");
        
        
        String key;
        String displayName;
        int len;
        for (String cur : availableContracts) {
            len = cur.length();
            displayName = cur.substring(3, len - 4);
            key = displayName.toLowerCase();
            values.put(key, cur + ".css");
            displayNames.add(new ColorTuple(displayName, key));
        }
    }
    
    @PreDestroy
    public void destroy() {
        if (null != values) {
            values.clear();
            values = null;
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("skinValuesManager");
                }
            }
        }
    }
    
    protected String getSkinCss(String skin) {
        if (!values.containsKey(skin))
            return getDefaultSkinCss();
        return values.get(skin);
    }

    protected String getDefaultSkinCss() {
        return values.get(defaultSkin);
    }

    public List<ColorTuple> getColorTuples() {
        return displayNames;
    }
    
    public static class ColorTuple {
        private String displayName;
        private String value;

        public ColorTuple(String displayName, String value) {
            this.displayName = displayName;
            this.value = value;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getValue() {
            return value;
        }
        
        
    }
    

    public int getSize() {
        return values.keySet().size();
    }

}
