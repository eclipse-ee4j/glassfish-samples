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

package enterprise.hello_stateless_client;

import javax.naming.InitialContext;
import enterprise.hello_stateless_ejb.StatelessSession;

public class StatelessJavaClient {

    public static void main(String args[]) {
        System.out.println("returnMessage():" + returnMessage());
        System.out.println("StatelessSession bean says : " + returnMessage());
    }

    public static String returnMessage() {

        try {

            InitialContext ic = new InitialContext();
            StatelessSession sless = 
                (StatelessSession) ic.lookup("enterprise.hello_stateless_ejb.StatelessSession");
			return (sless.hello());

        } catch(Exception e) {
            e.printStackTrace();
        }

	return null;
    }

}
