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

package transactional;

import javax.interceptor.InvocationContext;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.transaction.*;
import java.io.IOException;


@WebServlet(name="TransactionalServlet", urlPatterns={"/TransactionalServlet"})
public class TransactionalServlet extends HttpServlet {

 	public static ServletOutputStream m_out;
 	
 	@Inject
    UserTransaction userTransaction;
    
    @Inject
    BeanMandatory beanMandatory;
    
    @Inject
    BeanNever beanNever;
    
    @Inject
    BeanNotSupported beanNotSupported;
    
    @Inject
    BeanRequired beanRequired;
    
    @Inject
    BeanRequiresNew beanRequiresNew;
    
    @Inject
    BeanSupports beanSupports;
    

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String transactionalInterceptor = request.getParameter("TransactionalInterceptor");
		
    	m_out = response.getOutputStream();
    	m_out.println("<HTML>");
    	m_out.println("<HEAD>");
    	m_out.println("<title>CDI Sample Application for TransactionScoped Annotation</title>");
   	 	m_out.println("</HEAD>");
    	m_out.println("<BODY>");
    	m_out.println("TransactionalInterceptor value is -> " + transactionalInterceptor);
    	m_out.println("<BR><BR>");
		
        if (transactionalInterceptor.equalsIgnoreCase("MANDATORY")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction. Should get an error.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanMandatory.getId());
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println("<BR><BR>");
			} catch (Exception transactionalException) {
				if (transactionalException.getCause() instanceof TransactionRequiredException) {
					m_out.println("Got TransactionRequiredException for transactionalException.getCause() as expected.");
					m_out.println("<BR><BR>");
				} else {
					m_out.println("If you see this, it means there is something wrong!");
					m_out.println(transactionalException.getMessage());
					m_out.println("<BR><BR>");
				}
			}
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanMandatory.getId());
				m_out.println("<BR><BR>");
				userTransaction.commit();    			
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } else if (transactionalInterceptor.equalsIgnoreCase("NEVER")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction.</b>"); 
				m_out.println("<BR><BR>");
				m_out.println(beanNever.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction. Should get an error.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanNever.getId());
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println("<BR><BR>");
			} catch (Exception transactionalException) {					
				if (transactionalException.getCause() instanceof InvalidTransactionException) {
					m_out.println("Got InvalidTransactionException for transactionalException.getCause() as expected.");
					m_out.println("<BR><BR>");
				} else {
					m_out.println("If you see this, it means there is something wrong!");
					m_out.println(transactionalException.getMessage());
					m_out.println("<BR><BR>");
				}
			} finally {
				try {
            		userTransaction.rollback();
            	} catch (Exception e) {
            		m_out.println("Got unexpected exception in finally rollback for NEVER" + e.getMessage());
            	}
       		}
        } else if (transactionalInterceptor.equalsIgnoreCase("NOT_SUPPORTED")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction.</b>"); 
				m_out.println("<BR><BR>");
				m_out.println(beanNotSupported.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			} 
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction. Transaction is suspended during the method call. </b>");
				m_out.println("<BR><BR>");
				m_out.println(beanNotSupported.getId());
				userTransaction.commit();
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } else if (transactionalInterceptor.equalsIgnoreCase("REQUIRED")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction. Transaction would be started automatically for the method call.</b>"); 
				m_out.println("<BR><BR>");
				m_out.println(beanRequired.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			} 
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanRequired.getId());
				m_out.println("<BR><BR>");
				userTransaction.commit();
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } else  if (transactionalInterceptor.equalsIgnoreCase("REQUIRES_NEW")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction. Transaction would be started automatically for the method call.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanRequiresNew.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			} 
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction. NEW Transaction would be started automatically for the method call. </b>");
				m_out.println("<BR><BR>");
				m_out.println(beanRequiresNew.getId());
				m_out.println("<BR><BR>");
				userTransaction.commit();
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
        } else  if (transactionalInterceptor.equalsIgnoreCase("SUPPORTS")) {
			try {
				m_out.println("<b>Scenario: Invoking outside transaction. Method is executed outside transaction. </b>");
				m_out.println("<BR><BR>");
				m_out.println(beanSupports.getId());
				m_out.println("<BR><BR>");
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			} 
			try {
				userTransaction.begin();
				m_out.println("<b>Scenario: Invoking within a transaction. Method is executed within transaction context.</b>");
				m_out.println("<BR><BR>");
				m_out.println(beanSupports.getId());
				m_out.println("<BR><BR>");
				userTransaction.commit();
			} catch (Exception e){
				m_out.println("If you see this, it means there is something wrong!");
				m_out.println(e.getMessage());
				m_out.println("<BR><BR>");
			}
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
