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
package org.glassfish.servlet.non_blocking_io_read_war;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This Servlet acts as client to create HTTP connection to server, sends two
 * parts of data with 3 seconds sleeping in between, in order to simulate data
 * blocking.
 *
 * @author Daniel Guo
 */
@WebServlet(name = "ClientTest", urlPatterns = {"/"})
public class ClientTest extends HttpServlet {

    OutputStream output = null;
    InputStream input = null;

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

        out.println("<html>");
        out.println("<head>");
        out.println("<title>Non-blocking-read-war</title>");
        out.println("</head>");
        out.println("<body>");

        String urlPath = "http://"
                + request.getServerName()
                + ":" + request.getLocalPort() //default http port is 8080
                + request.getContextPath()
                + "/ServerTest";

        URL url = new URL(urlPath);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setChunkedStreamingMode(2);
        conn.setRequestProperty("Content-Type", "text/plain");
        conn.connect();

        try {
            output = conn.getOutputStream();
            // Sending the first part of data to server
            String firstPart = "Hello";
            out.println("Sending to server: " + firstPart + "</br>");
            out.flush();
            writeData(output, firstPart);

            Thread.sleep(2000);

            // Sending the second part of data to server
            String secondPart = "World";
            out.println("Sending to server: " + secondPart + "</br></br>");
            out.flush();
            writeData(output, secondPart);

            // Getting the echo data from server
            input = conn.getInputStream();
            printEchoData(out, input);

            out.println("Please check server log for detail");
            out.flush();
        } catch (IOException ioException) {
            Logger.getLogger(ReadListenerImpl.class.getName()).log(Level.SEVERE,
                    "Please check the connection or url path", ioException);
        } catch (InterruptedException interruptedException) {
            Logger.getLogger(ReadListenerImpl.class.getName()).log(Level.SEVERE,
                    "Thread sleeping error", interruptedException);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex) {
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (Exception ex) {
                }
            }
        }

        out.println("</body>");
        out.println("</html>");

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

    protected void writeData(OutputStream output, String data) throws IOException {
        if (data != null && !data.equals("") && output != null) {
            output.write(data.getBytes());
            output.flush();
        }
    }

    protected void printEchoData(PrintWriter out, InputStream input) throws IOException {
        while (input.available() > 0 && input != null && out != null) {
            out.print((char) input.read());
        }
        out.println("</br>");
    }
}
