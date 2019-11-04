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

package enterprise.servlet_stateless_ejb_test;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.HttpURLConnection;

import org.junit.*;

public class ServletStatelessEjbTest {

    @Test public void test1() {
        try {
            String url = "http://localhost:8080/servlet-stateless-war/servlet";
            URL u = new URL(url);
            HttpURLConnection c1 = (HttpURLConnection)u.openConnection();
            int code = c1.getResponseCode();
            InputStream is = c1.getInputStream();
            BufferedReader input = new BufferedReader (new InputStreamReader(is));
            String line = null;
            boolean status = true;
            while((line = input.readLine()) != null) {
                if (line.contains("Greeting from StatelessSessionBean:")) {
                    status = false;
                }
            }
            if(code != 200) {
                status = false;
            }

            Assert.assertEquals("Failed to invoke servlet", true, status);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("encountered exception in ServletStatelessEjbTest : test1");
        }
    }

    @Test public void test2() {
        try {
            String url = "http://localhost:8080/servlet-stateless-war/servlet?name=glassfish_user";
            URL u = new URL(url);
            HttpURLConnection c1 = (HttpURLConnection)u.openConnection();
            int code = c1.getResponseCode();
            InputStream is = c1.getInputStream();
            BufferedReader input = new BufferedReader (new InputStreamReader(is));
            String line = null;
            boolean status = false;
            while((line = input.readLine()) != null) {
                if (line.contains("Greeting from StatelessSessionBean:")) {
                    status = true;
                }
            }
            if(code != 200) {
                status = false;
            }

            Assert.assertEquals("Failed to invoke servlet", true, status);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("encountered exception in ServletStatelessEjbTest : test2");
        }
    }
}

