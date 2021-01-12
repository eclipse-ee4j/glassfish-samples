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
package org.glassfish.executor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Arun Gupta
 */
@WebServlet(name = "TestJNDIServlet", urlPatterns = {"/TestJNDIServlet"})
public class TestJNDIServlet extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TestJNDIServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestJNDIServlet at " + request.getContextPath() + "</h1>");
            out.println("<p>Getting ManagedExecutorService using JNDI lookup to submit or execute tasks...<br>");
            try {
                InitialContext ctx = new InitialContext();
                ManagedExecutorService executor = (ManagedExecutorService) ctx.lookup("java:comp/DefaultManagedExecutorService");
                for (int i = 0; i < 5; i++) {
                    out.format("submitting runnable(%d)<br>", i*3);
                    executor.submit(new MyRunnableTask(i*3));

                    out.format("executing runnable(%d)<br>", i*3 + 1);
                    executor.execute(new MyRunnableTask(i*3 + 1));

                    out.format("submitting callable(%d)<br>", i*3 + 2);
                    executor.submit(new MyCallableTask(i*3+2));
                }
                out.println("all tasks submitted<br/><br/>");
                out.println("Check server.log for output from the tasks.");
            } catch (NamingException ex) {
                out.println("Got exception: " + ex);
                Logger.getLogger(TestResourceServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            out.println("Check server.log for output from the task.");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
