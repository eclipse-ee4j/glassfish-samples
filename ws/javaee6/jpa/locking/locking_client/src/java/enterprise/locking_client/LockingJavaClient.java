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

package enterprise.locking_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class LockingJavaClient {

    // Sample data, which can be adjusted to see locking effect
    private final int NumConsumers = 6;
    private final int NumSuppliers = 3;
    private final int NumParts = 3;
    private final int NumUsers = NumConsumers + NumSuppliers;

    // server info
    private final String host = System.getProperty("javaee.server.name", "localhost");
    private final String port = System.getProperty("javaee.server.port", "8080");
    private final String contextRoot = "/locking";
    //Base URL to call Servlet
    private final String baseURL = "http://" + host + ":" + port + contextRoot + "/test/?tc=";

    public static void main(String args[]) throws Exception {
        System.out.println("LockingJavaClient: Test is starting");
        LockingJavaClient client = new LockingJavaClient();
        client.doTest();
        System.out.println("LockingJavaClient: Test is ended");
    }

    public void doTest() {
        try {
            initData();
            simulateParallelUpdates("updateWOL");
            simulateParallelUpdates("updateWPL");
        } catch (IOException ex) {
            System.out.println("Got Exception while executing test" + ex);
        }
    }

    /**
     * Initializes data required for this test.
     */
    private void initData() throws IOException {
        String url = baseURL + "initData" + "&nc=" + NumConsumers + "&ns=" + NumSuppliers + "&np=" + NumParts;
        callServlet(url);
    }

    /**
     * Call update for given userID using given updateMethod
     * @return true if the update succeeded false otherwise
     */
    private boolean callUpdate(String updateMethod, int userID) throws IOException {
        String url = baseURL + updateMethod + "&uid=" + userID;
        return callServlet(url);
    }

    /**
     * Calls the servlet with given url.
     * @return true if the call succeeded false otherwise
     */
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
                final String METHODRESULT_TOKEN = "MethodResult=";
                if (line.contains(METHODRESULT_TOKEN)) {
                    String result = line.substring(METHODRESULT_TOKEN.length());
                    callResult = Boolean.parseBoolean(result);
                }
            }
        } else {
            System.err.println("Unexpected return code: " + code);
        }
        return callResult;
    }

    /**
     * Simulate parallel updates for given operation using NumUsers
     */
    private void simulateParallelUpdates(final String operation) {
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < NumUsers; i++) {
            final int userId = i + 1; // userId are indexed at 1
            threads.add(new Thread() {
                public void run() {
                    try {
                        boolean updateResult = callUpdate(operation, userId);
                        System.out.println("Result for operation " + operation +
                                " for userId " + userId + " is " + (updateResult ? "Success" : "Failure"));

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
        }
        System.out.println("\nStarting parallel updates with " + NumUsers + " users for operation: " + operation);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < NumUsers; i++) {
            threads.get(i).start();
        }

        for (int i = 0; i < NumUsers; i++) {
            try {
                threads.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Parallel updates executed with " + NumUsers + " users for operation: " + operation + " Time taken:" + (endTime - startTime) + " miliseconds");
    }
}
