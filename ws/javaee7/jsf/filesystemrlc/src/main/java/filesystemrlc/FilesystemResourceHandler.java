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
package filesystemrlc;

import java.io.File;
import java.util.List;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;

public class FilesystemResourceHandler extends ResourceHandlerWrapper {
        
    private final File resourceDirectory;
    
    private final ResourceHandler resourceHandler;

    public FilesystemResourceHandler(ResourceHandler resourceHandler) {
        this.resourceHandler = resourceHandler;
        this.resourceDirectory = new File("/tmp/contracts");
    }

    @Override
    public ResourceHandler getWrapped() {
        return resourceHandler;
    }

    @Override
    public Resource createResource(String resourceName) {
        return resourceHandler.createResource(resourceName);
    }

    @Override
    public Resource createResource(String resourceName, String libraryName) {
        
        List<String> activeContracts = FacesContext.getCurrentInstance().getResourceLibraryContracts();

        if (!resourceName.startsWith("filesystemResourceHandler")) {
            if (activeContracts != null) {
                for(File file : this.resourceDirectory.listFiles()) {
                    for (String contract : activeContracts) {
                        if (contract.equals(file.getName())) {
                            if (libraryName != null) {
                                return new FilesystemResource(resourceDirectory, contract, libraryName, resourceName);
                            } else {
                                return new FilesystemResource(resourceDirectory, contract, null, resourceName);                    
                            }
                        }
                    }
                }
            }
        } else {
            resourceName = resourceName.substring(resourceName.indexOf("/") + 1);
            String contract = resourceName.substring(0, resourceName.indexOf("/"));
            resourceName = resourceName.substring(resourceName.indexOf("/") + 1);
            return new FilesystemResource(resourceDirectory, contract, null, resourceName);
        }
        
        return resourceHandler.createResource(resourceName, libraryName);
    }

    @Override
    public boolean isResourceURL(String url) {
        if (url.contains("filesystemResourceHandler")) {
            return true;
        }
        return resourceHandler.isResourceURL(url);
    }
}
