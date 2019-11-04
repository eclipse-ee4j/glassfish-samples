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

import javax.batch.api.chunk.ItemProcessor;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Properties;

@Named("SimpleItemProcessor")
public class SimpleItemProcessor
    implements ItemProcessor {

    @Inject
    private JobContext jobContext;


    public Object processItem(Object obj) throws Exception {
        Properties jobParameters = BatchRuntime.getJobOperator().getParameters(jobContext.getExecutionId());

        PayrollInputRecord inputRecord = (PayrollInputRecord) obj;
        PayrollRecord payrollRecord = new PayrollRecord();
        payrollRecord.setMonthYear((String) jobParameters.get("monthYear"));

        int base = inputRecord.getBaseSalary();
        float tax = base * 27 / 100.0f;
        float bonus = base * 15 / 100.0f;

        payrollRecord.setEmpID(inputRecord.getId());
        payrollRecord.setBase(base);
        payrollRecord.setTax(tax);
        payrollRecord.setBonus(bonus);
        payrollRecord.setNet(base + bonus - tax);
        
        return payrollRecord;
    }
    
}
