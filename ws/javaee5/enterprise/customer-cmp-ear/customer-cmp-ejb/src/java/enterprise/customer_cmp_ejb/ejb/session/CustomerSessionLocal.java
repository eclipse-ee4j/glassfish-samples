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
package enterprise.customer_cmp_ejb.ejb.session;

import javax.ejb.Local;
import enterprise.customer_cmp_ejb.persistence.*;
import enterprise.customer_cmp_ejb.common.*;

import java.util.List;

@Local
public interface CustomerSessionLocal {
    
    public Customer searchForCustomer(String id);
    
    public Subscription searchForSubscription(String id);
    
    public Address searchForAddress(String id);
    
    public void persist(Object obj);
    
    public List findAllSubscriptions();
    
    public List findCustomerByFirstName(String firstName);
    
    public List findCustomerByLastName(String lastName);
    
    public void remove(Object obj);
    
    public Customer addCustomerAddress(Customer cust, Address address); 

    public Customer removeCustomerSubscription(String cust, String subs) throws SubscriptionNotFoundException;
    
    public Customer addCustomerSubscription(String cust, String subs) throws DuplicateSubscriptionException;
}
