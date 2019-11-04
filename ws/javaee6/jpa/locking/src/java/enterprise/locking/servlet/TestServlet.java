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

package enterprise.locking.servlet;

import enterprise.locking.ejb.StatelessSessionBean;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="TestServlet", urlPatterns={"/test/*"})
public class TestServlet extends HttpServlet {

    @EJB
    private StatelessSessionBean testEJB;

    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        int thinkTime = 2; // Seconds
        try {
            out.println("TestServlet at " + request.getContextPath());
            String testcase = request.getParameter("tc");
            out.println("testcase = " + testcase);
            if ("initData".equals(testcase)) {

                // Step 1. initialize Data
                int numberOfConsumers = Integer.parseInt(request.getParameter("nc"));
                int numberOfSuppliers = Integer.parseInt(request.getParameter("ns"));
                int numberOfParts = Integer.parseInt(request.getParameter("np"));
                // ----------------------------------------
                // Populate test data
                testEJB.initData(numberOfConsumers, numberOfSuppliers, numberOfParts);
                out.println("MethodResult=" + Boolean.TRUE);

            } else if ("updateWOL".equals(testcase)) {

                // 2. update With Optimistic Locking with multiple user access
                String userID = request.getParameter("uid");
                int uID = Integer.parseInt(userID);
                // ----------------------------------------
                // Test Optimistic Locking for Version Object (Part).
                // Expect that only some updates will go through
                boolean updateResult = testEJB.updateWithOptimisticLock(uID, thinkTime);
                out.println("MethodResult=" + updateResult);

            } else if ("updateWPL".equals(testcase)) {

                // 4. update With Pessimistic Locking with multiple user access
                String userID = request.getParameter("uid");
                int uID = Integer.parseInt(userID);
                // ----------------------------------------
                // Test Pessimistic Locking for Version Object
                // Expect that all updates will go through
                boolean updateResult = testEJB.updateWithPessimisticLock(uID, thinkTime);
                out.println("MethodResult=" + updateResult);
                // ----------------------------------------

            } else {
                String invalidTestCaseMessage = "Invalid test case: " + testcase;
                System.out.println("Invalid test case: " + testcase);
                out.println(invalidTestCaseMessage);
            }
        } catch (Exception ex) {
            System.out.println("Failure in TestServlet");
        } finally {
            out.close();
        }
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }


    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "TestServlet";
    }


}
