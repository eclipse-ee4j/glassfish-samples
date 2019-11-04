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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * @author Arun Gupta
 */
public class MyTaskWithTransaction implements Runnable {
    
    private int id;

    public MyTaskWithTransaction() {
    }

    public MyTaskWithTransaction(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    @Resource UserTransaction tx;

    @Override
    public void run() {
        
        try {
            InitialContext context = new InitialContext();
            UserTransaction tx = (UserTransaction)context.lookup("java:comp/UserTransaction");
            
            try {
                System.out.format("%d (transaction): tx.start", id);
                tx.begin();
                System.out.format("%d (transaction): running", id);
                tx.commit();
                System.out.format("%d (transaction): tx.commit", id);
            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                Logger.getLogger(MyTaskWithTransaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NamingException ex) {
            Logger.getLogger(MyTaskWithTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
