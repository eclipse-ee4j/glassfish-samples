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
package org.glassfish.movieplex8.batch;

import java.util.List;
import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.operations.JobStartException;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.glassfish.movieplex8.entities.Sales;

/**
 * @author Arun Gupta
 */
@Stateless
@Named
public class SalesBean {
    
    @PersistenceContext EntityManager em;
    
    public void runJob() {
        try {
            JobOperator jo = BatchRuntime.getJobOperator();
            long jobId = jo.start("eod-sales", new Properties());
            System.out.println("Started job: with id: " + jobId);
        } catch (JobStartException ex) {
            ex.printStackTrace();
        }
    }
    
    public List<Sales> getSalesData() {
        return em.createNamedQuery("Sales.findAll", Sales.class).getResultList();
    }
}
