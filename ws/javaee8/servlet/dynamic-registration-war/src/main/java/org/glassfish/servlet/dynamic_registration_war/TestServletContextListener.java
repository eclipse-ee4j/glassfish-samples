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

import java.util.*;
import javax.servlet.*;

/**
 * ServletContextListener that registers a Servlet (with
 * name <tt>DynamicServlet</tt>) and Filter (with name 
 * <tt>DynamicFilter</tt>) in response to the <tt>contextInitialized</tt>
 * event.
 *
 * <p>The <tt>DynamicServlet</tt> is configured with an initialization
 * parameter and mapped to a URL pattern equal to <tt>/*</tt>.
 *
 * <p>The <tt>DynamicFilter</tt> is also configured with an initialization
 * parameter and mapped to the <tt>DynamicServlet</tt> such that it will
 * intercept any requests mapped to the <tt>DynamicServlet</tt>.
 *
 * @author Jan Luehe
 */
public class TestServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext sc = sce.getServletContext();

        // Register Servlet
        ServletRegistration sr = sc.addServlet("DynamicServlet",
            "org.glassfish.servlet.dynamic_registration_war.TestServlet");
        sr.setInitParameter("servletInitName", "servletInitValue");
        sr.addMapping("/*");

        // Register Filter
        FilterRegistration fr = sc.addFilter("DynamicFilter",
            "org.glassfish.servlet.dynamic_registration_war.TestFilter");
        fr.setInitParameter("filterInitName", "filterInitValue");
        fr.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST),
                                     true, "DynamicServlet");

        // Register ServletRequestListener
        sc.addListener("org.glassfish.servlet.dynamic_registration_war.TestServletRequestListener");
    }   

    @Override
    public void contextDestroyed(ServletContextEvent sce) {  
        // Do nothing
    }
}
