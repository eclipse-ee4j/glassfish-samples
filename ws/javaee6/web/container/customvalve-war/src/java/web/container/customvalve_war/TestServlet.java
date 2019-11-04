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

package web.container.customvalve_war;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * @author Jan Luehe
 */
public class TestServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        if (!"valvePropertyValue".equals(req.getAttribute("valveProperty"))) {
            throw new ServletException("Missing request attribute that was " +
                "supposed to have been set by custom valve configured in " +
                "sun-web.xml");
        }

        res.getWriter().println("HELLO WORLD!\n");
    }
}
