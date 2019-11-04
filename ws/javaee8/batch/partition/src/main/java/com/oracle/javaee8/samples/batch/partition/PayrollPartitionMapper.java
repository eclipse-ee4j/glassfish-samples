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
package com.oracle.javaee8.samples.batch.partition;

import javax.batch.api.partition.PartitionMapper;
import javax.batch.api.partition.PartitionPlan;
import javax.batch.api.partition.PartitionPlanImpl;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.context.JobContext;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Properties;

@Named("PayrollPartitionMapper")
public class PayrollPartitionMapper
    implements PartitionMapper {

    @Inject
    private JobContext jobContext;

    @EJB
    private SampleDataHolderBean bean;

    @Override
    public PartitionPlan mapPartitions() throws Exception {

        return new PartitionPlanImpl() {
            @Override
            public int getPartitions() {
                return 5;
            }

            @Override
            public Properties[] getPartitionProperties() {
                Properties jobParameters = BatchRuntime.getJobOperator().getParameters(jobContext.getExecutionId());

                String monthYear = (String) jobParameters.get("monthYear");
                int partitionSize = bean.getMaxEmployees() / getPartitions();
                                
                System.out.println("**[PayrollPartitionMapper] jobParameters: " + jobParameters
                    + "; executionId: " + jobContext.getExecutionId() + "; partitionSize = " + partitionSize);

                Properties[] props = new Properties[getPartitions()];
                for (int i=0; i<getPartitions(); i++) {
                    Properties partProps = new Properties();
                    partProps.put("monthYear", monthYear);
                    partProps.put("partitionNumber", i);
                    partProps.put("startEmpID", i * partitionSize);
                    partProps.put("endEmpID", (i + 1) * partitionSize);

                    props[i] = partProps;
                    System.out.println("**[PayrollPartitionMapper[" + i + "/" + getPartitions()
                                + "] : " + partProps);
                }

                return props;
            }
        };
    }

}
