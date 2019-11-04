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
package org.glassfish.servlet.non_blocking_io_write_war;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet acts as client to create HTTP connection to server, reads the
 * real time data from server
 *
 * @author Daniel Guo
 */
@WebServlet(name = "ClientTest", urlPatterns = {"/"})
public class ClientTest extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        BufferedReader input = null;
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Non-blocking-io-write-war</title>");
            out.println("</head>");
            out.println("<body>");

            String urlPath = "http://"
                    + request.getServerName()
                    + ":" + request.getLocalPort()
                    + request.getContextPath()
                    + "/ServerTest";

            URL url = new URL(urlPath);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setChunkedStreamingMode(2);
            conn.connect();

            out.println("This is the data from Server. </br>");
            out.flush();

            // Printing the data from server
            input = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            for (String line; input != null && (line = input.readLine()) != null;) {
                if (line.length() > 20) {
                    out.println(line.substring(0, 20) + "</br>");
                    out.flush();
                } else if (line.equals("---> Time out")) {
                    out.println(line + "</br>");
                    out.flush();
                } 
            }

            out.println("Check server log for more details");
            out.println("</body>");
            out.println("</html>");
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex) {
                }
            }
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
