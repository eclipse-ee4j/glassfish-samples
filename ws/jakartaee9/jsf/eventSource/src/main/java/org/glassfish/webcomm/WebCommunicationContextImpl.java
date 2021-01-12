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

package org.glassfish.webcomm;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebCommunicationHandler;

/**
 * WebCommunicationContextImpl class
 * @author Santiago.PericasGeertsen@oracle.com
 */
public class WebCommunicationContextImpl implements WebCommunicationContext {

    private String path;

    private Set<WebCommunicationHandler> handlers;

    public WebCommunicationContextImpl(String path) {
        this.path = path;
        handlers = new CopyOnWriteArraySet<WebCommunicationHandler>();
    }

    public String getPath() {
        return path;
    }

    public Set<WebCommunicationHandler> getHandlers() {
        return handlers;
    }

}
