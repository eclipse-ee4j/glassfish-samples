/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.movieplex8.batch;

import java.util.List;
import jakarta.batch.api.chunk.AbstractItemWriter;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.glassfish.movieplex8.entities.Sales;

/**
 * @author Arun Gupta
 */
@Dependent
@Named
public class SalesWriter extends AbstractItemWriter {
    
    @PersistenceContext EntityManager em;

    @Override
    @Transactional
    public void writeItems(List list) {
        for (Sales s : (List<Sales>)list) {
            System.out.println("SalesWriter.writeItem: " + s);
            em.persist(s);
        }
    }
}
