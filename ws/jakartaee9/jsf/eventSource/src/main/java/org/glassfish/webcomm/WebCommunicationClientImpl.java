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

import jakarta.servlet.http.HttpSession;
import org.glassfish.webcomm.api.WebCommunicationClient;
import java.security.Principal;

/**
 * ServerSentEventClientImpl class.
 * @author Santiago.PericasGeertsen@oracle.com
 */
public class WebCommunicationClientImpl extends WebCommunicationClient {

    private String remoteAddr;

    private String remoteHost;

    private int remotePort = -1;

    private HttpSession httpSession;

    private Principal userPricipal;

    private String remoteUser;

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteUser() {
        return remoteUser;
    }


    public void setRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public Principal getUserPrincipal() {
        return userPricipal;
    }

    public void setUserPricipal(Principal userPricipal) {
            this.userPricipal = userPricipal;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }


}
