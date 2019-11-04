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

package enterprise.simple_wsdl_client;

import javax.xml.ws.WebServiceRef;

public class HelloClient {
    /*
    @WebServiceRef(wsdlLocation="http://localhost:8080/simple-ws-war/HelloService?wsdl")
    static HelloService service;
    */
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            HelloClient client = new HelloClient();
            client.doTest(args);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void doTest(String[] args) {
        try {
            System.out.println("Retrieving the port from the hello service: ");
            Hello port = new HelloService().getHelloPort();
            System.out.println("Invoking the sayHello operation on the port.");

            String name;
            if (args.length > 0) {
                name = args[0];
            } else {
                name = "No Name";
            }
            
            String response = port.sayHello(name);
            System.out.println(response);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

