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

package enterprise.hello_singleton_ejb_test;

import org.junit.*;

import enterprise.hello_singleton_ejb.*;

public class HelloSingletonEjbTest {

  @Test public void echoHello() {

	try {

        HelloSingletonImplService service = new HelloSingletonImplService();
        HelloSingletonImpl port = service.getHelloSingletonImplPort();

        System.out.println("Returned value " +port.hello() );



        Assert.assertEquals(
                        "Invalid return message",
                        "hello, world"  ,
                        port.hello());




	} catch(Exception e) {
		e.printStackTrace();
		Assert.fail("encountered exception in HelloSingletonEjbTest:returnMessage");
	}
  }

}
