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
package transactionscoped;

import javax.enterprise.context.ContextNotActiveException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.*;
import java.io.IOException;

@WebServlet(name = "TransactionScopedServlet", urlPatterns = {"/TransactionScopedServlet"})
public class TransactionScopedServlet extends HttpServlet {

    public static ServletOutputStream m_out;
    @Inject
    UserTransaction userTransaction;
    @Inject
    Bean1 bean1;
    @Inject
    Bean1 bean1_1;
    @Inject
    Bean2 bean2;

     /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        m_out = response.getOutputStream();
        m_out.println("<HTML>");
        m_out.println("<HEAD>");
        m_out.println("<title>CDI Sample Application for TransactionScoped Annotation</title>");
        m_out.println("</HEAD>");
        m_out.println("<BODY>");

        try {
            userTransaction.begin();
            
            m_out.println("<BR>");
            m_out.println("<BR>");
            m_out.println("<b>Scenario 1: Starting transaction and calling beans. Bean 1 is called twice but object id should be the same.</b>");
            m_out.println("<BR>");
            m_out.println("<BR>");

            m_out.println(bean1.getId());
            m_out.println("<BR>");
            m_out.println("<BR>");

            m_out.println(bean1_1.getId());
            m_out.println("<BR>");
            m_out.println("<BR>");

            m_out.println(bean2.getId());

            userTransaction.commit();
        } catch (Exception e) {
            m_out.println("If you see this, it means there is something wrong!");
            m_out.println(e.getMessage());
        }

        try {
            userTransaction.begin();
            
            m_out.println("<BR>");
            m_out.println("<BR>");
            m_out.println("<b>Scenario 2: Repeat of scenario 1 with new transaction. Should see different object ids. </b>");
            m_out.println("<BR>");
            m_out.println("<BR>");

            m_out.println(bean1.getId());
            m_out.println("<BR>");
            m_out.println("<BR>");

            m_out.println(bean1_1.getId());
            m_out.println("<BR>");
            m_out.println("<BR>");

            m_out.println(bean2.getId());

            userTransaction.commit();
        } catch (Exception e) {
            m_out.println("If you see this, it means there is something wrong!");
            m_out.println(e.getMessage());
        }

        m_out.println("<BR>");
        m_out.println("<BR>");
        m_out.println("<b>Scenario 3: Calling TransactionScoped bean outside transaction.</b>");
        m_out.println("<BR>");
        m_out.println("<BR>");
        try {
            bean1.getId();
            m_out.println("If you see this, it means there is something wrong!");
        } catch (ContextNotActiveException cnae) {
            m_out.println("Got a ContextNotActiveException as expected.");
            m_out.println(cnae.getMessage());
        }

        m_out.println("</BODY>");
        m_out.println("</HTML>");
    }
    
    /** 
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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