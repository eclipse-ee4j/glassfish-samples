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
package org.glassfish.executor;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Arun Gupta
 */
public class MyCallableTask implements Callable<Product> {

    private int id;
    
    public MyCallableTask(int id) {
        this.id = id;
    }
    
    @Override
    public Product call() {
        try {
            System.out.format("%d (callable): starting", id);
            String moduleName = null;
            try {
                // Try doing JNDI lookup inside a task
                Context ctx = new InitialContext();
                moduleName = (String) ctx.lookup("java:module/ModuleName");
            } catch (NamingException ex) {
                Logger.getLogger(MyCallableTask.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.format("%d (callable): Module name is %s. sleeping 2 seconds", id, moduleName);
            Thread.sleep(2000);
            System.out.format("%d (callable): complete", id);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestResourceServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Product(id);
    }
}
