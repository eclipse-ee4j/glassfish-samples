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
package com.oracle.javaee8.samples.batch.api;

public class PayrollRecord {
    
    private String monthYear;
    
    private int empID;

    private int base;
    
    private float tax;
    
    private float bonus = 0;
    
    private float net;
    
    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }
    
    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float getBonus() {
        return bonus;
    }

    public void setBonus(float bonus) {
        this.bonus = bonus;
    }

    public float getNet() {
        return net;
    }

    public void setNet(float net) {
        this.net = net;
    }
    
    @Override
    public int hashCode() {
        return empID;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PayrollRecord)) {
            return false;
        }
        PayrollRecord other = (PayrollRecord) object;
        return empID == other.empID && monthYear.equals(other.monthYear);
    }

    @Override
    public String toString() {
        return "com.oracle.javaee7.samples.batch.api.PayrollRecord[ id= [" + monthYear + ":" + empID + "]";
    }
    
}
