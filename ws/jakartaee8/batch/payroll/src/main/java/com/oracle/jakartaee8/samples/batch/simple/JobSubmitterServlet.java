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
package com.oracle.jakartaee8.samples.batch.simple;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author makannan
 */
public class JobSubmitterServlet extends HttpServlet {
    
    @EJB
    SampleDataHolderBean initializer; 

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
            pw.println("<title>Servlet JobSubmitterServlet V2</title>");
            pw.println("</head>");
            pw.println("<body>");

            pw.println("<p>To run this sample, first select a month from the drop down list and click 'Display Input Records' button</p>");

            pw.println("<p>Once the input records are displayed, click the 'Calculate Payroll' button at the bottom of this page</p>");

            String origSelectedMonthYear = request.getParameter("inputMonthYear");
            String selectedMonthYear = origSelectedMonthYear == null
                ? "JAN-2013" : origSelectedMonthYear;

            displayInputRecords(pw, selectedMonthYear);
            displayPayrollForm(pw, selectedMonthYear);
            if (request.getParameter("calculatePayroll") != null) {
                submitJobFromXML("PayrollJob", selectedMonthYear);
                displayPayrollData(pw, selectedMonthYear);
            }

            pw.println("</body>");
            pw.println("</html>");
        } catch (Exception ex) {
            throw new ServletException(ex);
        } finally {
            pw.close();
        }
    }

    private void displayInputRecords(PrintWriter pw, String selectedMonthYear)
        throws Exception {
        String[] monthYear = initializer.getAllMonthYear();
        pw.println("<form name=\"monthYearForm\">");
        pw.println("<table border=\"yes\">");
        pw.println("<tr><td colspan=\"2\">Input Records For");
        pw.println("<select name=\"inputMonthYear\">");
        for (String monYr : monthYear) {
            pw.println("<option " + (monYr.equals(selectedMonthYear) ? "Selected" : " ")
                    + " value=\"" + monYr +"\">" + monYr + "</option>");
        }
        pw.println("</select><input type=hidden name=\"inputMonthYear\" value=\"" + selectedMonthYear + "\">");
        pw.println("</select><input type=submit name=\"DisplayInputRecords\" value=\"Display Input Records\"></td></tr>");
        pw.println("<tr><td>MONTH-YEAR</td><td>EMP ID</td><td>Base Salary</td></tr>");
        for (PayrollInputRecord r : initializer.getPayrollInputRecords(selectedMonthYear)) {
            pw.println("<tr><td>" + selectedMonthYear + "</td>"
                    + "<td>" + r.getId() + "</td>"
                    + "<td>" + r.getBaseSalary() + "</td></tr>");
        }

        pw.println("</table >");
        pw.println("</form >");
    }

    private void displayPayrollForm(PrintWriter pw, String monthYear) {
        pw.println("<form>");
        pw.println("<table>");
        pw.println("<tr><td>Calculate Payroll for the month of: " + monthYear + "</td>");
        pw.println("</select><input type=hidden name=\"inputMonthYear\" value=\"" + monthYear + "\">");
        pw.println("<td><input type=\"submit\" name=\"calculatePayroll\" value=\"Calculate Payroll\"/></td></tr>");
        pw.println("</table>");
        pw.println("</form>");
    }

    private void displayPayrollData(PrintWriter pw, String monthYear) {
        pw.println("<table>");
        pw.println("<tr><td>Processed Payroll Records</td></tr>");
        pw.println("<table border=\"yes\">");
        pw.println("<tr><td>MONTH-YEAR</td><td>EMP ID</td>"
                + "<td>Base Salary</td><td>Bonus</td><td>Tax</td>"
                + "<td>Net</td>"
                    + "</tr>");
        for (PayrollRecord r : initializer.getPayrollRecords(monthYear)) {
            pw.println("<tr><td>" + r.getMonthYear() + "</td>");
            pw.println("<td>" + r.getEmpID() + "</td>");
            pw.println("<td>" + r.getBase() + "</td>");
            pw.println("<td>" + r.getBonus() + "</td>");
            pw.println("<td>" + r.getTax() + "</td>");
            pw.println("<td>" + r.getNet() + "</td></tr>");
        }
        pw.println("</table>");
        pw.println("</table>");
    }

    private long submitJobFromXML(String jobName, String selectedMonthYear)
            throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();

        Properties props = new Properties();
        props.setProperty("Month-Year", selectedMonthYear);

        long executionID = jobOperator.start(jobName, props);

        try { Thread.sleep(3000); } catch (Exception ex) {}

        return executionID;
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
