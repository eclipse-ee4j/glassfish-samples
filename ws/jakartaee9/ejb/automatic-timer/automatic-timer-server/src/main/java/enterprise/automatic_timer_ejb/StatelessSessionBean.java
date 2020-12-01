/*
 	Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
	
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


package enterprise.automatic_timer_ejb;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.ejb.Schedule;
import jakarta.ejb.Timer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.PersistenceContext;

import enterprise.automatic_timer_ejb.persistence.LogRecord;

@Stateless
public class StatelessSessionBean implements StatelessSession {

    @PersistenceContext EntityManager em;

    @Schedule(second="*/3", minute="*", hour="*", info="Automatic Timer Test")
    public void test_automatic_timer(Timer t) {
        long count = (Long)em.createNamedQuery("LogRecord.countLoggedTimeouts").getSingleResult();

        System.out.println("Call # " + (count + 1));
        if (count > 10) {
            throw new IllegalStateException("Too many timeouts received: " + count);
        } else if (count == 10) {
            LogRecord lr = new LogRecord("Canceling timer " + t.getInfo() + " at " + new Date());
            em.persist(lr);
            t.cancel();
            System.out.println("Done");
        } else {
            LogRecord lr = new LogRecord("" + t.getInfo() + " timeout received at " + new Date());
            em.persist(lr);
        }
    }

    public List<String> getRecords() {
        return (List<String>)em.createNamedQuery("LogRecord.findAllRecords").getResultList();
    }
}
