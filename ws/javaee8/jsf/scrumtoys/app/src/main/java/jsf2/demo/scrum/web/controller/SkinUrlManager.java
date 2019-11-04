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
import javax.inject.Named;
import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;

/**
 * @author eder
 */
@Named("skinUrlManager")
@RequestScoped
public class SkinUrlManager extends AbstractManager implements Serializable {

    private String skin;

    @Inject
    private SkinManager skinManager;
    @Inject
    private SkinValuesManager skinValuesManager;
    private static final long serialVersionUID = 7770408832569218016L;

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public void update() {
        if (skin == null || "".equals(skin))
            return;
        String skinCss = skinValuesManager.getSkinCss(skin.toLowerCase());
        skinManager.setSelectedSkin(skinCss);
    }

    public SkinManager getSkinManager() {
        return skinManager;
    }

    public void setSkinManager(SkinManager skinManager) {
        this.skinManager = skinManager;
    }

    public SkinValuesManager getSkinValuesManager() {
        return skinValuesManager;
    }

    public void setSkinValuesManager(SkinValuesManager skinValuesManager) {
        this.skinValuesManager = skinValuesManager;
    }

}
