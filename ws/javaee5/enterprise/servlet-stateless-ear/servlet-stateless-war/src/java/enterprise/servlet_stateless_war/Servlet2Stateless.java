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

package enterprise.servlet_stateless_war;

import java.io.*;

import javax.ejb.EJB;

import javax.servlet.*;
import javax.servlet.http.*;

import javax.naming.*;

import enterprise.servlet_stateless_ejb.*;

// Though it is perfectly fine to declare the dependency on the bean
// at the type level, it is not required for stateless session bean
// Hence the next two lines are commented and we rely on the
// container to inject the bean.
// @EJB(name="StatelessSession", beanInterface=StatelessSession.class)

public class Servlet2Stateless
    extends HttpServlet {

    // Using injection for Stateless session bean is still thread-safe since
    // the ejb container will route every request to different
    // bean instances. However, for Stateful session beans the
    // dependency on the bean must be declared at the type level

    @EJB
    private StatelessSession sless;

    public void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        try {

            out.println("<HTML> <HEAD> <TITLE> Servlet Output </TITLE> </HEAD> <BODY BGCOLOR=white>");
            out.println("<CENTER> <FONT size=+1> Servlet2Stateless:: Please enter your name </FONT> </CENTER> <p> ");
            out.println("<form method=\"POST\">");
            out.println("<TABLE>");
            out.println("<tr><td>Name: </td>");
            out.println("<td><input type=\"text\" name=\"name\"> </td>");
            out.println("</tr><tr><td></td>");
            out.println("<td><input type=\"submit\" name=\"sub\"> </td>");
            out.println("</tr>");
            out.println("</TABLE>");
            out.println("</form>");
            String val = req.getParameter("name");

            if ((val != null) && (val.trim().length() > 0)) {
                out
                        .println("<FONT size=+1 color=red> Greeting from StatelessSessionBean: </FONT> "
                                + sless.sayHello(val) + "<br>");
            }
            out.println("</BODY> </HTML> ");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("webclient servlet test failed");
            throw new ServletException(ex);
        }
    }

}
