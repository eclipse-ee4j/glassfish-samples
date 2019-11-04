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
package enterprise.annotation_override_interceptor_appclient;

import java.util.List;

import javax.ejb.EJB;
import enterprise.annotation_override_interceptor_ejb.*;

public class StatelessSessionAppClient {

    @EJB
    private static StatelessSession sless;

    public static void main(String args[]) {
	try {
            sless.initUpperCase("hello, World!!");
            sless.initLowerCase("Build.xml");
        } catch (Exception  badEx) {
		badEx.printStackTrace();
        }

        List<String> upperList = sless.getInterceptorNamesFor("initUpperCase");
	printList("initUpperCase", upperList);

	try {
	    sless.isOddNumber(7);
        } catch (Exception  badEx) {
		badEx.printStackTrace();
        }

	List<String> isOddNumberList = sless.getInterceptorNamesFor("isOddNumber");
	printList("isOddNumber", isOddNumberList);
    }

    private static void printList(String msg, List<String> list) {
	System.out.print("Interceptors invoked for " + msg + "(): ");
	String delimiter = "";
        for (String str : list) {
            System.out.print(delimiter + str);
	    delimiter = ", ";
        }
	System.out.println("}");
    }
}
