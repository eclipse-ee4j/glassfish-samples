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
package com.oracle.jakartaee8.samples.batch.simple;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import javax.batch.api.chunk.AbstractItemReader;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;

@Named("SimpleItemReader")
public class SimpleItemReader
    extends AbstractItemReader {

    @EJB
    private SampleDataHolderBean dataBean;

    @Inject
    private JobContext jobContext;

    Iterator<PayrollInputRecord> payrollInputRecords;

    public void open(Serializable e) throws Exception {
        Properties jobParameters = BatchRuntime.getJobOperator().getParameters(jobContext.getExecutionId());
        Set<PayrollInputRecord> records = dataBean.getPayrollInputRecords(
                (String) jobParameters.get("Month-Year"));
        payrollInputRecords = records.iterator();
    }

    public Object readItem() throws Exception {
        return payrollInputRecords.hasNext() ? payrollInputRecords.next() : null;
    }
    
}
