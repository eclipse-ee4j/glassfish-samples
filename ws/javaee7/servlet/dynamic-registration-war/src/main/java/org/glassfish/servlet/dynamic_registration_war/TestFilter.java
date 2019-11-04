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

package org.glassfish.servlet.dynamic_registration_war;

import java.io.*;
import javax.servlet.*;

/**
 * Filter that is registered by the
 * <tt>web.servlet.dynamicregistration_war.TestServletContextListener</tt>.
 *
 * <p>This Filter retrieves (from its <tt>FilterConfig</tt>) the
 * initialization parameter that was added by the
 * <tt>web.servlet.dynamicregistration_war.TestServletContextListener</tt>
 * when it registered the Filter, and stores this initialization parameter
 * in the request (as a request attribute), so it can be read by the Servlet
 * to which this Filter was mapped.
 *
 * @author Jan Luehe
 */
public class TestFilter implements Filter {

    private String filterInitParam;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterInitParam = filterConfig.getInitParameter("filterInitName");
    }   

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        req.setAttribute("filterInitName", filterInitParam);
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
