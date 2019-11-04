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

package web.servlet.annotation_war;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class illustrates WebServlet annotation.
 *
 * @author Shing Wai Chan
 */
@WebServlet(name="mytest",
        urlPatterns={"/"},
        initParams={ @WebInitParam(name="message", value="my servlet") } )
public class TestServlet extends HttpServlet {
    private String listenerMessage = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        listenerMessage = (String)config.getServletContext().getAttribute("listenerMessage");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        PrintWriter writer = res.getWriter();
        writer.write("Hello, " + getInitParameter("message") + ", ");
        writer.write(req.getAttribute("filterMessage") + ", ");
        writer.write(listenerMessage + ".\n");
    }
}
