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
package enterprise.interceptor_stateless_ejb_test;

import org.junit.*;

import javax.naming.InitialContext;
import enterprise.interceptor_stateless_ejb.StatelessSession;
import enterprise.interceptor_stateless_ejb.BadArgumentException;

public class InterceptorStatelessEjbTest {

    @Test public void test1() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            Assert.assertEquals("Failed to change case",
                sless.initUpperCase("duke"), "Duke");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test2() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            String retVal = sless.initUpperCase("4duke");
            Assert.assertEquals("Failed to change case", "A", "B");
        } catch(BadArgumentException badEx) {
            Assert.assertEquals("FailedtoConvert", "a", "a");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test3() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            String retVal = sless.initUpperCase(" duke");
            Assert.assertEquals("Failed to change case", "A", "B");
        } catch(BadArgumentException badEx) {
            Assert.assertEquals("FailedtoConvert", "a", "a");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test4() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            String retVal = sless.initUpperCase("\nDuke");
            Assert.assertEquals("Failed to change case", "A", "B");
        } catch(BadArgumentException badEx) {
            Assert.assertEquals("FailedtoConvert", "a", "a");
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

    @Test public void test5() {
        try {
            InitialContext ic = new InitialContext();
            String jndiName = "enterprise.interceptor_stateless_ejb.StatelessSession#"
                + "enterprise.interceptor_stateless_ejb.StatelessSession";

            StatelessSession sless = (StatelessSession) ic.lookup(jndiName);
            String retVal = sless.initUpperCase("Duke");
            Assert.assertEquals("Failed to change case", "Duke", retVal);
        } catch(Exception e) {
            e.printStackTrace();
            Assert.fail("encountered exception in InterceptorStatelessEjbTest:returnMessage");
        }
    }

}
