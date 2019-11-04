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

package enterprise.hello_webserviceref_client1;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.soap.MTOM;

import enterprise.hello_webserviceref_client1.HelloService;
import enterprise.hello_webserviceref_client1.Hello;

@WebServlet(name = "webclient", urlPatterns = {"/"})
public class TestServlet extends HttpServlet {
    @MTOM
    @javax.xml.ws.WebServiceRef(HelloService.class)
    Hello port;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {

        try {
            //HelloService service = new HelloService();
            //Hello port = service.getHelloPort();
            String userName = "Duke";
            if (req.getParameter("userName") != null) {
                userName = req.getParameter("userName");
            }

            byte[] ret = port.getHelloBinary(userName.getBytes("UTF-8"));
            PrintWriter writer = res.getWriter();
            writer.write(new String(ret,"UTF-8"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}
