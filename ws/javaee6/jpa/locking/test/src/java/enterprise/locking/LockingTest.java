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

package enterprise.locking;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * This test class exercises the Locking demo application
 */
public class LockingTest {


    // System property values used to configure the HTTP connection
    private String contextRoot = "/locking";
    private String host = "localhost";
    private String port = "8080";
    private String baseURL = null;


    /**
     * Set up instance variables required by this test case.
     */
    @Before
    public void setUp() throws Exception {
        host = System.getProperty("javaee.server.name");
        port = System.getProperty("javaee.server.port");
        // System.out.println("host="+host+", port="+port);
        baseURL = "http://" + host + ":" + port + contextRoot + "/test/?tc=";
    }

    @Test
    public void lockingTest() throws Exception {
        System.out.println("lockingTest: Test is starting.");
	boolean updateResult = false;
        int userId = 1;
        try {
 	    updateResult = callUpdate("updateWOL", userId);
            Assert.assertTrue("updateWOL", updateResult);
 	    updateResult = callUpdate("updateWPL", userId);
            Assert.assertTrue("updateWPL", updateResult);
        } catch (Exception ex) {
            System.out.println("Got Ex");
            // ex.printStackTrace();
        }
        System.out.println("lockingTest: Test is ended.");
    }

    private boolean callUpdate(String updateMethod, int userID) throws IOException {
        String url = baseURL + updateMethod + "&uid=" + userID;
        return callServlet(url);
    }

    private boolean callServlet(String url) throws IOException {
        boolean callResult = false;
        System.out.println("Calling URL:" + url);
        HttpURLConnection conn = (HttpURLConnection)
                (new URL(url)).openConnection();
        int code = conn.getResponseCode();
        if (code == 200) {
            InputStream is = conn.getInputStream();
            BufferedReader input = new BufferedReader(
                    new InputStreamReader(is));
            String line = null;
            while ((line = input.readLine()) != null) {
                final String METHODRESULT = "MethodResult=";
                if (line.contains(METHODRESULT)) {
                    String result = line.substring(METHODRESULT.length());
                    callResult = Boolean.parseBoolean(result);
                }
            }
        } else {
            System.err.println("Unexpected return code: " + code);
        }
        return callResult;
    }

}

