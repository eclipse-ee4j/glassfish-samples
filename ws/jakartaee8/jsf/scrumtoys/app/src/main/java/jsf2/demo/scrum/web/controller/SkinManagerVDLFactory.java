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
import java.util.ArrayList;
import java.util.List;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import javax.faces.view.ViewDeclarationLanguageWrapper;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
public class SkinManagerVDLFactory extends ViewDeclarationLanguageFactory {
    
    private ViewDeclarationLanguageFactory wrapped;
    @Inject
    private SkinManager skinManager;
    public SkinManagerVDLFactory(ViewDeclarationLanguageFactory wrapped) {
        this.wrapped = wrapped;
    }
    @Override
    public ViewDeclarationLanguageFactory getWrapped() {
        return this.wrapped;
    }
    
    public ViewDeclarationLanguage getViewDeclarationLanguage(String viewId) {
        return new VDL(wrapped.getViewDeclarationLanguage(viewId));
    }
    private class VDL extends ViewDeclarationLanguageWrapper {
        private ViewDeclarationLanguage wrapped;
        private VDL(ViewDeclarationLanguage wrapped) {
            this.wrapped = wrapped;
        }
        @Override
        public ViewDeclarationLanguage getWrapped() {
            return wrapped;
        }
        @Override
        public List<String> calculateResourceLibraryContracts(FacesContext context, String viewId) {
            List<String> result = getWrapped().calculateResourceLibraryContracts(context, viewId);
            String selectedSkin = SkinManagerVDLFactory.this.skinManager.getSelectedSkin();
            if (null != selectedSkin) {
                assert(selectedSkin.endsWith(".css"));
                selectedSkin = selectedSkin.substring(0, selectedSkin.length() - 4);
                for (String cur : result) {
                    if (selectedSkin.equals(cur)) {
                        result = new ArrayList<String>(1);
                        result.add(cur);
                        break;
                    }
                }
            }
            return result;
        }
    }
    
    
}
