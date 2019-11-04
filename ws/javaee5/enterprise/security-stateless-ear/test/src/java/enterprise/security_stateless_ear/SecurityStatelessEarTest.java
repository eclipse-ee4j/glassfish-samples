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

package enterprise.security_stateless_ear;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.appserv.security.ProgrammaticLogin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

import enterprise.security_stateless_ejb.Sless;

public class SecurityStatelessEarTest {
    private static Sless sless = null;

    @BeforeClass
    public static void setup() throws NamingException {
        InitialContext ic = new InitialContext();
        ProgrammaticLogin plogin = new ProgrammaticLogin();
        plogin.login("javaee", "javaee");
        sless = (Sless)ic.lookup("enterprise.security_stateless_ejb.Sless");
    }

    @Test
    public void helloRolesAllowed() {
        try {
            assertEquals(
                "Invalid return message",
                "SlessEJB.helloRolesAllowed(): Hello World",
                sless.helloRolesAllowed());
        } catch(Exception e) {
            e.printStackTrace();
            fail("encountered exception in SecurityStatelessEarTest:helloRolesAllowed");
        }
    }

    @Test(expected=Exception.class)
    public void helloRolesAllowed2() {
        sless.helloRolesAllowed2();
    }

    @Test
    public void helloPermitAll() {
        try {
            assertEquals(
                "Invalid return message",
                "SlessEJB.helloPermitAll(): Hello World",
                sless.helloPermitAll());
        } catch(Exception e) {
            e.printStackTrace();
            fail("encountered exception in SecurityStatelessEarTest:helloPermitAll");
        }
    }

    @Test(expected=Exception.class)
    public void helloDenyAll() {
        sless.helloDenyAll();
    }
}
