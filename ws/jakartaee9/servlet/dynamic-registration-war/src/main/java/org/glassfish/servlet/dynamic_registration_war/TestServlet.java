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

package org.glassfish.servlet.dynamic_registration_war;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

/**
 * Servlet that is registered by the
 * <tt>web.servlet.dynamicregistration_war.TestServletContextListener</tt>.
 * <p>This Servlet verifies that the initialization parameter that was
 * added by the <tt>TestServletContextListener</tt> when it registered the
 * Servlet is present in its <tt>ServetConfig</tt>.
 * <p>The Servlet also verifies that the Filter mapped to it has been
 * invoked, by checking the request for the presence of the Filter's
 * initialization parameter that was added by the
 * <tt>TestServletContextListener</tt> when it registered the Filter,
 * and was set on the request (as a request attribute) by the Filter as
 * the request passed through the Filter.
 * 
 * <p>If any of the verification steps fail, the Servlet will throw an
 * Exception. Otherwise, it outputs the string <tt>HELLO WORLD!</tt> to the
 * response.
 * @author Jan Luehe
 */
public class TestServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        if (!"servletInitValue".equals(getServletConfig().getInitParameter(
                        "servletInitName"))) {
            throw new ServletException("Missing servlet init param");
        }

        if (!"filterInitValue".equals(req.getAttribute("filterInitName"))) {
            throw new ServletException("Missing request attribute that was " +
                "supposed to have been set by programmtically registered " +
                "Filter");
        }

        if (!"listenerAttributeValue".equals(req.getAttribute(
                "listenerAttributeName"))) {
            throw new ServletException("Missing request attribute that was " +
                "supposed to have been set by programmtically registered " +
                "ServletRequestListener");
        }

        res.getWriter().println("HELLO WORLD! GLASSFISH\n");
    }
}
