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

package enterprise.customer_cmp_appclient;

import javax.naming.InitialContext;
import javax.ejb.EJB;
import enterprise.customer_cmp_ejb.ejb.session.CustomerSessionRemote;
import enterprise.customer_cmp_ejb.persistence.*;
        
public class CustomerAppClient {

    @EJB 
    private static CustomerSessionRemote sess;

    public static void main(String args[]) {
	try {
		InitialContext ic = new InitialContext();
                String CUSTOMER_ID="99999";
                
                System.out.println("Searching for customer with id:"+CUSTOMER_ID);
                Customer searchedCustomer= sess.searchForCustomer(CUSTOMER_ID);
                
                if(searchedCustomer==null){
                    throw new Exception("searched customer not found");
                }
                
                System.out.println("found customer with id:"+CUSTOMER_ID);
                System.out.println("First Name:"+searchedCustomer.getFirstName());
                System.out.println("Last Name:"+searchedCustomer.getLastName());
                
	} catch(Exception e) {
		e.printStackTrace();
	}
  }

}
