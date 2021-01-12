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


package ejb.embedded.client;

import ejb.embedded.SimpleEjb;

import jakarta.ejb.embeddable.EJBContainer;
import javax.naming.Context;

public class TestClient {

    private static String appName;

    public static void main(String[] s) throws Exception {
        TestClient t = new TestClient();
        t.test();

    }

    private void test() throws Exception {

        EJBContainer c = null;
        try {
            c = EJBContainer.createEJBContainer();
            Context ic = c.getContext();
            System.out.println("Looking up EJB...");
            SimpleEjb ejb = (SimpleEjb) ic.lookup("java:global/ejb-embedded/SimpleEjb");
            System.out.println("Invoking EJB...");
            System.out.println("Inserting entities...");
            ejb.insert(5);
            System.out.println("JPA count returned: " + ejb.verify());
            System.out.println("Done calling EJB");
        } finally {
            if (c != null) {
                System.out.println("Closing container ...");
                c.close();
                System.out.println("Done Closing container");
            }
        }

        System.out.println("..........FINISHED Embedded test");
    }
}
