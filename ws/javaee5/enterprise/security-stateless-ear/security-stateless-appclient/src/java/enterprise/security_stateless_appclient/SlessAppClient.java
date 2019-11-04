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

package enterprise.security_stateless_appclient;

import javax.ejb.EJB;
import enterprise.security_stateless_ejb.Sless;

public class SlessAppClient {
    @EJB private static Sless sless;

    public static void main(String args[]) {
        System.out.println(sless.helloRolesAllowed());

        try {
             sless.helloRolesAllowed2();
             throw new IllegalStateException(
                 "Unexpected succesful call for helloRolesAllowed2()");
        } catch(Exception ex) {
             System.out.println("Expected Exception for sless.helloRolesAllowed2()");
        }

        System.out.println(sless.helloPermitAll());

        try {
             sless.helloDenyAll();
             throw new IllegalStateException(
                 "Unexpected succesful call for helloDenyAll()");
        } catch(Exception ex) {
             System.out.println("Expected Exception for sless.helloDenyAll()");
        }
    }
}
