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
package com.oracle.javaee7.samples.batch.partition;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author makannan
 */
@Singleton
@Startup
public class SampleDataHolderBean {

    private static final int MAX_EMPLOYEES = 30;

    private Map<String, ConcurrentSkipListMap<Integer, PayrollInputRecord>> payrollInputRecords
            = new HashMap<>();

    private Map<String, Set<PayrollRecord>> payrollRegistry
            = new HashMap<>();
    
    @PostConstruct
    public void onApplicationStartup() {
        try {
        String[] monthYear = new String[] {"JAN-2013", "FEB-2013", "MAR-2013"};
        for (int monthIndex = 0; monthIndex < monthYear.length; monthIndex++) {
            ConcurrentSkipListMap<Integer, PayrollInputRecord> inputRecords = new ConcurrentSkipListMap<>();
            for (int empID=1; empID<MAX_EMPLOYEES; empID++) {
                PayrollInputRecord e = new PayrollInputRecord();
                e.setId(empID);
                int baseSalary = 10000 + (empID % 20) + monthIndex*100;
                e.setBaseSalary(baseSalary);
                inputRecords.put(empID, e);
            }

            payrollInputRecords.put(monthYear[monthIndex], inputRecords);
            System.out.println("**Added " + monthYear + ": " + inputRecords);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getMaxEmployees() {
        return MAX_EMPLOYEES;
    }

    public String[] getAllMonthYear() {
        return payrollInputRecords.keySet().toArray(new String[0]);
    }

    public Collection<PayrollInputRecord> getPayrollInputRecords(String monthYear) {
        return payrollInputRecords.get(monthYear).values();
    }

    public Iterator<PayrollInputRecord> getPayrollInputRecords(String monthYear,
            int startId, int endId) {
        ConcurrentSkipListMap<Integer, PayrollInputRecord> map = payrollInputRecords.get(monthYear);       
        return map.subMap(startId, endId).values().iterator();
    }
    
    public void addPayrollRecord(PayrollRecord r) {
        String monthYear = r.getMonthYear();
        Set<PayrollRecord> monthlyPayroll = payrollRegistry.get(monthYear);
        if (monthlyPayroll == null) {
            monthlyPayroll = new HashSet<>();
            payrollRegistry.put(monthYear, monthlyPayroll);
        }
        monthlyPayroll.add(r);
    }
    
    public Set<PayrollRecord> getPayrollRecords(String monthYear) {
        Set<PayrollRecord> empty = new HashSet<>();
        Set<PayrollRecord> records = payrollRegistry.get(monthYear);
        return records == null ? empty : records;
    }
}
