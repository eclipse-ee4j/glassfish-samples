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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.servlet.server_push_war;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpMethodConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.PushBuilder;
import jakarta.servlet.annotation.ServletSecurity.TransportGuarantee;

/**
 * This class illustrates HTTP/2 Server Push.
 *
 * @author Shing Wai Chan
 */
@WebServlet(urlPatterns="")
@ServletSecurity(httpMethodConstraints={
        @HttpMethodConstraint(value="GET", transportGuarantee=TransportGuarantee.CONFIDENTIAL) })
public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        PushBuilder pushBuilder = req.newPushBuilder()
                .path("my.css");
        pushBuilder.push();
        res.getWriter().println("<html><head><title>HTTP2 Test</title><link rel=\"stylesheet\" href=\"my.css\"></head><body>Hello</body></html>");
    }
}
