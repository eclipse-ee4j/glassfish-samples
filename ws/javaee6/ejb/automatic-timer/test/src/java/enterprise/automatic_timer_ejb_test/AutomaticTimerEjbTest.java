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

package enterprise.automatic_timer_ejb_test;

import org.junit.*;
import java.util.List;

import javax.naming.InitialContext;
import enterprise.automatic_timer_ejb.StatelessSession;

public class AutomaticTimerEjbTest {

  @Test public void verifyRecords() {
	try {
		InitialContext ic = new InitialContext();
		StatelessSession sless =
			(StatelessSession) ic.lookup("java:global/automatic-timer-ejb/StatelessSessionBean");
                System.out.println("Waiting for the timer to expire");
                Thread.sleep(7000);
                List<String> result = sless.getRecords();
                System.out.println("Got " + result.size() + " log records:");
                for (String s : result) {
                    System.out.println(s);
                }

		Assert.assertTrue("Too many timeouts", (result.size() <= 11));
	} catch(Exception e) {
		e.printStackTrace();
		Assert.fail("encountered exception in HelloStatelessEjbTest:returnMessage");
	}
  }

}
