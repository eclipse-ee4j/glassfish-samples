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
import java.util.Set;

@Entity
@Table(name = "T_PART")
public class Part implements java.io.Serializable {

    // Instance variables
    private int id;
    private String name;
    private int amount;
    private int verNum;
    private Set<User> users;

    public Part() {
    }

    public Part(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Part(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    // ==================================================
    // getters and setters for the state fields

    @Id
    @Column(name = "PID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(length = 20, name = "PNAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "AMOUNT")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }


    @Version
    @Column(name = "VER")
    public int getVerNum() {
        return verNum;
    }

    public void setVerNum(int verNum) {
        this.verNum = verNum;
    }

    // ===========================================================
    // getters and setters for the association fields
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "part")
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }


    public String toString() {
        return "Product id=" + getId() + ", amount=" + getAmount();
    }

}

