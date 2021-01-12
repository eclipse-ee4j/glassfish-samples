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

package ejb.embedded;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.annotation.security.PermitAll;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import ejb.embedded.persistence.SimpleEntity;

/**
 * @author mvatkina
 */
@Stateless
public class SimpleEjb {

    @PersistenceContext(unitName="embedded_test") EntityManager em;

    @PermitAll
    public int verify() {
        String result = null;
        Query q = em.createNamedQuery("SimpleEntity.findAll");
        Collection entities = q.getResultList();
        int s = entities.size();
        for (Object o : entities) {
            SimpleEntity se = (SimpleEntity)o;
            System.out.println("Found: " + se.getName());
        }

        return s;
   }

    @PermitAll
    public void insert(int num) {
        for (int i = 1; i <= num; i++) {
            System.out.println("Inserting # " + i);
            SimpleEntity e = new SimpleEntity(i);
            em.persist(e);
        }
    }
}
