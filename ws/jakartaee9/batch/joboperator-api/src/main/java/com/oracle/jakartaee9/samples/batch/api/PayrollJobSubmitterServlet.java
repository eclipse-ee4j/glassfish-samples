/*
 	Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
	
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
package com.oracle.jakartaee9.samples.batch.api;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.JobExecution;
import jakarta.batch.runtime.JobInstance;
import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author makannan
 */
public class PayrollJobSubmitterServlet extends HttpServlet {
    
    @EJB
    SampleDataHolderBean initializer;

    @Inject
    private SimpleLock simpleLock;

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
        PrintWriter pw = response.getWriter();
        try {
            pw.println("<html>");
            pw.println("<head>");
            pw.println("<title>Sample Batch application demonstrating JobOperator API</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<h1>Sample Batch application demonstrating JobOperator API</h1>");
            pw.println("<p>This sample application, submits a batch job that performs payroll processing for the selected month.</p>");
            pw.println("<p>To run, first select a month and click 'Calculate Payroll'. This will submit a batch job. Each execution will");
            pw.println("</br> create a new JobExecution with an associated unique executionID. The table below will list details about each job execution.</p>");

            String origSelectedMonthYear = request.getParameter("inputMonthYear");
            String selectedMonthYear = origSelectedMonthYear == null
                ? "JAN-2013" : origSelectedMonthYear;
            if (request.getParameter("calculatePayroll") != null) {
                submitJobFromXML("PayrollJob", selectedMonthYear);
            }
            displayPayrollForm(pw, selectedMonthYear);
            displayJobDetails(pw);

            pw.println("</body>");
            pw.println("</html>");
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            pw.close();
        }
    }

    private void displayPayrollForm(PrintWriter pw, String monthYear) {
        pw.println("<form>");
        pw.println("Calculate Payroll for the month of: ");
        pw.println("<select name=\"inputMonthYear\">");
        for (String monYr : initializer.getAllMonthYear()) {
            pw.println("<option " + (monYr.equals(monthYear) ? "Selected" : " ")
                    + " value=\"" + monYr +"\">" + monYr + "</option>");
        }
        pw.println("<td><input type=\"submit\" name=\"calculatePayroll\" value=\"Calculate Payroll\"/></td></tr>");
        pw.println("<td><input type=\"submit\" name=\"refresh\" value=\"refresh\"/></td></tr>");
        pw.println("</table>");
        pw.println("</form>");
    }

    private void displayJobDetails(PrintWriter pw) {
        pw.println("<table>");
        pw.println("<tr><td>Status of Submitted Jobs</td></tr>");
        pw.println("<table border=\"yes\">");
        pw.println("<tr><td>Job Name</td><td>ExecutionID</td>"
                + "<td>Batch Status</td><td>Exit Status</td>"
                + "<td>Start Time Status</td><td>End Time</td>"
                    + "</tr>");

        JobOperator jobOperator = BatchRuntime.getJobOperator();
        try {
            for (JobInstance jobInstance : jobOperator.getJobInstances("payroll", 0, Integer.MAX_VALUE-1)) {
                for (JobExecution jobExecution : jobOperator.getJobExecutions(jobInstance)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("<tr>");
                    sb.append("<td>").append(jobExecution.getJobName()).append("</td>");
                    sb.append("<td>").append(jobExecution.getExecutionId()).append("</td>");
                    sb.append("<td>").append(jobExecution.getBatchStatus()).append("</td>");
                    sb.append("<td>").append(jobExecution.getExitStatus()).append("</td>");
                    sb.append("<td>").append(jobExecution.getStartTime()).append("</td>");
                    sb.append("<td>").append(jobExecution.getEndTime()).append("</td>");
                    pw.println(sb.toString());
                }
            }
        } catch (Exception ex) {

        }
        pw.println("</table>");
        pw.println("</table>");
    }

    private long submitJobFromXML(String jobName, String selectedMonthYear)
            throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();

        Properties props = new Properties();
        props.setProperty("monthYear", selectedMonthYear);

        return jobOperator.start(jobName, props);
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
