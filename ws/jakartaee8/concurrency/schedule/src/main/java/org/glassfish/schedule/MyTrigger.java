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
package org.glassfish.schedule;

import java.util.Date;
import javax.enterprise.concurrent.LastExecution;
import javax.enterprise.concurrent.Trigger;

/**
 * @author Arun Gupta
 */
public class MyTrigger implements Trigger {

    int runCounter = 0;
    
    // A Trigger that runs 5 seconds after task was initially submitted, or
    // 5 seconds after previous run time.
    
    @Override
    public Date getNextRunTime(LastExecution le, Date taskScheduledDate) {
        if (le == null) {
            return new Date(taskScheduledDate.getTime() + 5000);
        }
        if (System.currentTimeMillis() > taskScheduledDate.getTime() + 30000L) {
            // stops task 30 seconds after task was originally scheduled
            System.out.println("Trigger done");
            return null;
        }
        return new Date(le.getRunStart().getTime() + 5000);
    }

    @Override
    public boolean skipRun(LastExecution le, Date date) {
        return false;
    }

}
