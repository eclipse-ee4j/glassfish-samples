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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet acts as Client to illustrate the handshake process for HTTP
 * upgrade
 *
 * @author Daniel
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

        final String CRLF = "\r\n";
        final String host = request.getServerName();// "localhost";
        final int port = request.getServerPort(); // 8080;
        final String contextRoot = "/http-upgrade-war";
        final String Data = "Hello World";
        InputStream input = null;
        OutputStream output = null;
        BufferedReader reader = null;
        Socket s = null;

        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ClientTest</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Http Upgrade Process</h1>");


            // Setting the HTTP upgrade request header
            String reqStr = "POST " + contextRoot + "/ServerTest HTTP/1.1" + CRLF;
            reqStr += "User-Agent: Java/1.7" + CRLF;
            reqStr += "Host: " + host + ":" + port + CRLF;
            reqStr += "Accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2" + CRLF;
            reqStr += "Upgrade: Dummy Protocol" + CRLF;
            reqStr += "Connection: Upgrade" + CRLF;
            reqStr += "Content-type: application/x-www-form-urlencoded" + CRLF;
            reqStr += "Transfer-Encoding: chunked" + CRLF;
            reqStr += CRLF;
            reqStr += Data + CRLF;

            // Create socket connection to ServerTest
            s = new Socket(host, port);
            input = s.getInputStream();
            output = s.getOutputStream();

            // Send request header with data
            output.write(reqStr.getBytes());
            output.flush();

            out.println("<h2>Sending upgrade request to server......</h2>");
            out.println("<h3>Request header with data:</h3>");
            out.println();

            String reqStrDisplay = reqStr.replaceAll("\r\n", "</br>");
            out.println(reqStrDisplay);
            out.println("--------------------------------------- </br></br>");
            out.flush();

            reader = new BufferedReader(new InputStreamReader(input));

            out.println("<h2>Server accept upgrade request, send back the response:</h2>");
            out.println("<h3>Response header:</h3>");
            out.println();

            // Reading the response, and displaying the header from server
            printHeader(reader, out);

            out.println("--------------------------------------- </br></br>");
            out.flush();

            out.println("<h2>Server send back the response with new protocol and data:</h2>");
            out.println("<h3>Response header with data:</h3>");

            // Reading the response, and displaying the header from server
            printHeader(reader, out);

            // Reading the echo data
            String dataOutput;
            if ((dataOutput = reader.readLine()) != null) {
                // Print out the data after header
                out.println("</br>" + dataOutput + "</br>");
                out.println("--------------------------------------- </br></br>");
                out.println("<h2>Connection with new protocol established</h2>");
            }
            out.flush();
            out.println("</body>");
            out.println("</html>");
        } catch (IOException ioException) {
            Logger.getLogger(ProtocolUpgradeHandler.class.getName()).log(Level.SEVERE, null, ioException);
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (s != null) {
                s.close();
            }
        }
    }

    protected void printHeader(BufferedReader reader, PrintWriter out) throws IOException {
        for (String line; (line = reader.readLine()) != null;) {
            if (line.isEmpty()) {
                break; // Stop when headers are completed.
            }
            out.println(line + "</br>");
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
