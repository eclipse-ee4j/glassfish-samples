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
package enterprise.customer_cmp_ejb.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.*;


@Entity
@NamedQuery(name="findAllSubscriptions", query="select s from Subscription s")

public  class Subscription implements java.io.Serializable{

    private String title;
    private String type;
    private Collection<Customer> customers;
    
    public Subscription(){
        
    }
    
    @Id
    public String getTitle(){ //primary key
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
    
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type=type;
    }

    //access methods for cmr fields
    @ManyToMany(mappedBy="subscriptions")
    public Collection<Customer> getCustomers(){
        return customers;
    }
    public void setCustomers(Collection<Customer> customers){
        this.customers=customers;
    }

    public Subscription (
            String title,
            String type) {

        if (type.equals(SubscriptionType.MAGAZINE)) {
            _create(title,SubscriptionType.MAGAZINE);
        } 
        else if (type.equals(SubscriptionType.JOURNAL)) {
            _create(title,SubscriptionType.JOURNAL);
        } 
        else if (type.equals(SubscriptionType.NEWS_PAPER)) {
            _create(title,SubscriptionType.NEWS_PAPER);
        }
        else
            _create(title,SubscriptionType.OTHER);

    }
    

    private String _create (
            String title,
            String type)  {
        

        setTitle(title);
        setType(type);
        return title;
    }
    
}


    
