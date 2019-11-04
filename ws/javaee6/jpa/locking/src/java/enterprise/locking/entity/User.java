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

package enterprise.locking.entity;

import javax.persistence.*;

@Entity
@Table(name = "T_USER")
public class User implements java.io.Serializable {
    public enum UserType {
        CONSUMER, SUPPLIER
    }

    private int id;
    private String firstName;
    private String lastName;
    private UserType userType;
    private int count;
    private Part part;

    public User() {
    }

    public User(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(int id, String firstName, String lastName, UserType userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
    }

    //=======================================================
    // getters and setters for the state fields
    @Id
    @Column(name = "UID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(length = 20, name = "FNAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(length = 20, name = "LNAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "UTYPE")
    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    @Column(name = "COUNT")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    // ===========================================================
    // getters and setters for the association fields
    @ManyToOne
    @JoinColumn(name = "PID")
    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public String toString() {
        return "User id=" + getId()
                + ", lastName=" + getLastName()
                + ", userType=" + getUserType();
    }

}


