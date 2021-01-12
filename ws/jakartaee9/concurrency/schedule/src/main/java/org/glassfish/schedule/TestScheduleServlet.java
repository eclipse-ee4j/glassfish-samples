/*
 	Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
	
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
package org.glassfish.schedule;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author arungup
 */
@WebServlet(name = "TestScheduleServlet", urlPatterns = {"/TestScheduleServlet"})
public class TestScheduleServlet extends HttpServlet {

    @Resource
    ManagedScheduledExecutorService executor;

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
            out.println("<title>Servlet TestScheduleServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestScheduleServlet at " + request.getContextPath() + "</h1>");
            out.printf("<p>Scheduling a Callable to be run 5 seconds later at %s", new Date(System.currentTimeMillis()));
            ScheduledFuture<Product> future = executor.schedule(new MyCallableTask(1), 5, TimeUnit.SECONDS);
            while (true) {
                if (future.isDone()) {
                    break;
                } else {
                    System.out.println("Checking Callable Future, waiting for 1 sec");
                    Thread.sleep(1000);
                }
            }
            out.printf("<p>Callable Task completed at %s. Result is: %d", new Date(System.currentTimeMillis()), future.get().getId());
            
            out.printf("<p>Scheduling a Runnable to be run 5 seconds later at %s", new Date(System.currentTimeMillis()));
            ScheduledFuture<?> f = executor.schedule(new MyRunnableTask(2), 5, TimeUnit.SECONDS);
            while (true) {
                if (f.isDone()) {
                    break;
                } else {
                    System.out.println("Checking Runnable Future, waiting for 1 sec");
                    Thread.sleep(1000);
                }
            }
            out.printf("<p>Runnable Task completed at %s.", new Date(System.currentTimeMillis()));
            out.println("</body>");
            out.println("</html>");
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(TestScheduleServlet.class.getName()).log(Level.SEVERE, null, ex);
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
