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
package org.glassfish.servlet.http_upgrade_war;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet acts as Server to illustrate what server will do when get the
 * HTTP Upgrade Request
 *
 * @author Daniel
 *
 */
@WebServlet(name = "ServerTest", urlPatterns = {"/ServerTest"})
public class ServerTest extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Checking request header
        if ("Dummy Protocol".equals(request.getHeader("Upgrade"))) {
            System.out.println("Accept to upgrade");
            response.setStatus(101);
            response.setHeader("X-Powered-By", "Servlet/3.1 "
                    + "(GlassFish Server Open Source Edition 4.x Java/Oracle Corporation");
            response.setHeader("Upgrade", "Dummy Protocol");
            response.setHeader("Connection", "Upgrade");
            response.flushBuffer();

            // Call the upgrade API, and invoke the upgrade handler
            ProtocolUpgradeHandler handler = request.upgrade(ProtocolUpgradeHandler.class);
        } else {
            response.setStatus(400);
            response.setHeader("X-Powered-By", "Servlet/3.1 "
                    + "(GlassFish Server Open Source Edition 4.x Java/Oracle Corporation)");
            response.setHeader("Connection", "Refused");
            response.sendError(400, "The Upgrade request sent by the client was incorrect or can not be accept by the server");
            System.out.println("Upgrade field is: " + request.getHeader("Upgrade"));
            System.out.println("Upgrade refused");
            
        }
    }

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
