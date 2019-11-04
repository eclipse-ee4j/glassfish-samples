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

import java.util.Collection;
import java.util.Vector;

import javax.persistence.*;


@Entity
//name defaults to the unqualified entity class name.        
//default access is property.
    
public class Address implements java.io.Serializable{
    
    private String addressID;
    private String street;
    private String city;
    private String zip;
    private String state;
    
    public Address(String id, String street, String city, String zip, String state){

        setAddressID(id);
        setStreet(street);
        setCity(city);
        setZip(zip);
        setState(state);

    }
    
    public Address(){
    
    }
    
    @Id //this is the default 
    @Column(name="addressID")
    public String getAddressID(){      //primary key
        return addressID;
    }
    public void setAddressID(String id){
        this.addressID=id;
    }
    
    //will default to Street
    public String getStreet(){
        return street;
    }
    public void setStreet(String street){
        this.street=street;
    }

    //will default to city
    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city=city;
    }

    //will default to zip
    public String getZip(){
        return zip;
    }
    public void setZip(String zip){
        this.zip=zip;
    }

    //will default to state
    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state=state;
    }
    
    
}
