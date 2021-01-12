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

package filesystemrlc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import jakarta.faces.application.Resource;
import jakarta.faces.context.FacesContext;

public class FilesystemResource extends Resource {

    private final File baseDir;
    
    private final String contract;
    
    public FilesystemResource(File baseDir, String contract, String libraryName, String resourceName) {
        this.baseDir = baseDir;
        this.contract = contract;
        setLibraryName(libraryName);
        setResourceName(resourceName);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        /*
         * Be aware you need to make sure you clean libraryName and resourceName here,
         * this code is merely a proof of concept!
         */
        File resourceFile = new File(baseDir, 
            contract + "/" +
            (getLibraryName() != null ? getLibraryName() + "/" : "") +
            getResourceName());
        return new FileInputStream(resourceFile);
    }

    @Override
    public Map<String, String> getResponseHeaders() {
        return Collections.EMPTY_MAP;
    }

    @Override
    public String getRequestPath() {
        /*
         * Note this URL should take prefix mapping or extension mapping into account,
         * right now assuming you have mapped /faces/*
         */
        return FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + 
                "/faces/jakarta.faces.resource/filesystemResourceHandler/" +
                contract + "/" +
                (getLibraryName() != null ? getLibraryName() + "/" : "") +
                getResourceName();
    }

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public boolean userAgentNeedsUpdate(FacesContext context) {
        return true;
    }
}
