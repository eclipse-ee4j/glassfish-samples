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

package ejb.ejb32.war;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ejb.EJB;

/**
 * 
 */
@WebServlet(name="TestServlet",
	    urlPatterns={"/"})
public class TestServlet extends HttpServlet {

    private @EJB PropertiesBean propertiesBean;
    private @EJB HelloBean helloBean;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
	System.out.println("In TestServlet::init()");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        PrintWriter writer = res.getWriter();

	String titleMsg = propertiesBean.getProperty("title.message");
        writer.println(titleMsg);

	String helloMsg = helloBean.sayHello();
	writer.println("HelloBean says : " + helloMsg);

	int numPropertyAccesses = propertiesBean.getAccessCount();
	writer.println("Singleton property access count = " + 
		       numPropertyAccesses);
	
    }
}
