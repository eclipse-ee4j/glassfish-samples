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
package enterprise.interceptor_stateless_appclient;

import javax.ejb.EJB;
import enterprise.interceptor_stateless_ejb.*;

public class StatelessSessionAppClient {

    @EJB
    private static StatelessSession sless;

    public static void main(String args[]) {

        tryToConvert("duke");
        tryToConvert(" duke");
        tryToConvert(null);
        tryToConvert(" duke");
        tryToConvert("4duke");
        tryToConvert("Duke");
    }

    private static void tryToConvert(String inputStr) {
        try {
            String upCaseStr = sless.initUpperCase(inputStr);
            System.out.println("Converted " + inputStr + " to " + upCaseStr);
        } catch (BadArgumentException badEx) {
            System.out.println("Cannot convert (BadArgument)" + inputStr);
        } catch (Exception ex) {
            System.out.println("Got some exception while converting: "
                    + inputStr);
        }
    }

}
