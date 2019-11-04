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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;


/**
 *
 * @author edermag
 */
@ManagedBean(name="skinValuesManager", eager=true)
@ApplicationScoped
public class SkinValuesManager implements Serializable {

    private Map<String, String> values;

    private String defaultSkin = "blue";
    private static final long serialVersionUID = 2238251086172648511L;

    @PostConstruct
    public void construct() {
        values = new LinkedHashMap<String, String>();
        values.put("yellow", "appYellowSkin.css");
        values.put("orange", "appOrangeSkin.css");
        values.put("red", "appRedSkin.css");
        values.put(defaultSkin, "appBlueSkin.css");
    }

    @PreDestroy
    public void destroy() {
        if (null != values) {
            values.clear();
            values = null;
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("skinValuesManager");
    }
    
    protected String getSkinCss(String skin) {
        if (!values.containsKey(skin))
            return getDefaultSkinCss();
        return values.get(skin);
    }

    protected String getDefaultSkinCss() {
        return values.get(defaultSkin);
    }

    public List<String> getNames() {
        return new ArrayList<String>(values == null ? null : values.keySet());
    }

    public int getSize() {
        return values.keySet().size();
    }

}
