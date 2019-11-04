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

package bank.test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 *
 * @author Manveen Kaur
 */
public class TestBank extends TestCase {
    
    public TestBank(String name) {
        super(name);
    }
    
    public static void main(String[] args) {
        
        System.out.println("Invoking Secure Bank Service...");
        
        TestRunner.run(new TestSuite(TestBank.class));
    }
    
    public static void testDeposit() {
        try {
            bank.client.BankAccountService service = new bank.client.BankAccountService();
            bank.client.BankAccount port = service.getBankAccountPort();
            
            double amount = 1000.0;
            double originalBalance = port.getBalance();
            double result = port.deposit(amount);
            
            assertEquals(result, amount + originalBalance);
            
        } catch(Exception e) {
            fail("Test case failed due to exception");
            e.printStackTrace();
        }
    }
    
}
