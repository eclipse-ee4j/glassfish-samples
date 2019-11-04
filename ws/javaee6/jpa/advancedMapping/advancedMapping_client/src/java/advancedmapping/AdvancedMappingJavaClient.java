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

package advancedmapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class AdvancedMappingJavaClient {

    // server info
    private final String host = System.getProperty("javaee.server.name", "localhost");
    private final String port = System.getProperty("javaee.server.port", "8080");
    private final String contextRoot = "/advancedMapping";
    //Base URL to call Servlet
    private final String baseURL = "http://" + host + ":" + port + contextRoot + "/test/?tc=";

    public static void main(String args[]) throws Exception {
        System.out.println("advancedMappingJavaClient: Test is starting");
        AdvancedMappingJavaClient client = new AdvancedMappingJavaClient();
        client.doTest();
        System.out.println("advancedMappingJavaClient: Test is ended");
    }

    public void doTest() {
        try {
            createData();
            queryData();
        } catch (IOException ex) {
            System.out.println("Got Exception while executing test" + ex);
        }
    }

    /**
     * Initializes data required for this test.
     */
    private void createData() throws IOException {
        String url = baseURL + "createData";
        callServlet(url);
    }

    private void queryData() throws IOException {
        String url = baseURL + "queryData";
        callServlet(url);
    }

    /**
     * Calls the servlet with given url.
     * @return true if the call succeeded false otherwise
     */
    private void callServlet(String url) throws IOException {
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
                System.out.println(line);
            }
        } else {
            System.err.println("Unexpected return code: " + code);
        }
    }

}
