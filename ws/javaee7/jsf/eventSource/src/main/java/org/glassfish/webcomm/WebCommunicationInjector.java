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

package org.glassfish.webcomm;

import java.lang.reflect.Field;
import java.util.Map;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebCommunicationHandler;
import org.glassfish.webcomm.annotation.WebHandlerContext;

/**
 * WebCommunicationInjector class.
 *
 */
public class WebCommunicationInjector {

    private static WebCommunicationInjector INSTANCE = new WebCommunicationInjector();

    private WebCommunicationInjector() {
    }

    public static WebCommunicationInjector getInstance() {
        return INSTANCE;
    }

    public void inject(WebCommunicationHandler wch,
            Map<String, WebCommunicationContext> contexts)
    {
        Class<? extends WebCommunicationHandler> clazz =
                wch.getClass();
        try {
            for (Field f : clazz.getDeclaredFields()) {
                WebHandlerContext an = f.getAnnotation(WebHandlerContext.class);
                if (an != null) {
                    WebCommunicationContext wcc = contexts.get(
                            normalizePath(an.value()));
                    assert wcc != null;
                    f.setAccessible(true);
                    f.set(wch, wcc);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String normalizePath(String path) {
        path = path.trim();
        return path.startsWith("/") ? path : "/" + path;
    }
}
