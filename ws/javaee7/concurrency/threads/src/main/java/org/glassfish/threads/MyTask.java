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
package org.glassfish.threads;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Arun Gupta
 */
public class MyTask implements Runnable {

    private int id;

    public MyTask(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Context ctx = new InitialContext();
            String appName = (String) ctx.lookup("java:app/AppName");
            System.out.printf("%d:Running Task for app: '%s' ", id, appName);
        } catch (NamingException ex) {
            Logger.getLogger(MyTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
