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

package webbeansservlet;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * A simple Web Bean that performs a login operation with user's 
 * credentials. 
 */
@Named
@SessionScoped
@Default
public class Login implements Serializable {

    @Inject Credentials credentials;

    private boolean loggedIn = false;

    /**
     * This is where you could potentially access a database.
     */
    public void login() {
        if ((credentials.getUsername() != null &&
            credentials.getUsername().trim().length() > 0) &&
            (credentials.getPassword() != null &&
            credentials.getPassword().trim().length() > 0)) {
            loggedIn = true;
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

}
