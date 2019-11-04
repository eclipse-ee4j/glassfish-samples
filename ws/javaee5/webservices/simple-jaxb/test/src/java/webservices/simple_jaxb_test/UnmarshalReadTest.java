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

package webservices.simple_jaxb_test;

import org.junit.*;

import javax.naming.InitialContext;
import webservices.simple_jaxb.*;

import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class UnmarshalReadTest {

  @Test public void returnMessage() {
	try {
		System.out.println("name = [" + UnmarshalRead.getName() + "]");
		Assert.assertEquals(
			"Invalid return message",
			"Alice Smith",
			UnmarshalRead.getName());
	} catch(Exception e) {
		e.printStackTrace();
		Assert.fail("encountered exception in HelloStatelessEjbTest:returnMessage");
	}
  }

}
