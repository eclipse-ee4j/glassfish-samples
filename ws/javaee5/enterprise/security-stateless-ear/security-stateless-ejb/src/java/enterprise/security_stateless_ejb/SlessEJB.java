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

package enterprise.security_stateless_ejb;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

@Stateless
public class SlessEJB implements Sless {

    @RolesAllowed("javaee")
    public String helloRolesAllowed() {
        return "SlessEJB.helloRolesAllowed(): Hello World";
    }

    @RolesAllowed("noauthuser")
    public String helloRolesAllowed2() {
        return "SlessEJB.helloRoleAllowed2(): Hello World";
    }

    @PermitAll
    public String helloPermitAll() {
        return "SlessEJB.helloPermitAll(): Hello World";
    }

    @DenyAll
    public String helloDenyAll() {
        return "SlessEJB.helloDenyAll(): Hello World";
    }
}
