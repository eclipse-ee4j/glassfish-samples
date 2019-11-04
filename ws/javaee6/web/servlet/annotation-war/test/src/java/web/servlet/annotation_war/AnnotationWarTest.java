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

package web.servlet.annotation_war;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.*;

/**
 * This is a junit test for annotation-war.
 *
 * @author Shing Wai Chan
 */
public class AnnotationWarTest {
    private static final String CONTEXT_PATH = "/annotation-war";

    private String host = null;
    private int port = 8080;

    @Before
    public void setUpClass() throws Exception {
        host = System.getProperty("javaee.server.name");
        port = Integer.parseInt(System.getProperty("javaee.server.port"));
    }

    @Test public void testWebClient() throws Exception {
        String url = "http://" + host + ":" + port + CONTEXT_PATH;
        HttpURLConnection conn = null;
        BufferedReader input = null;
        try {
            conn = (HttpURLConnection)(new URL(url)).openConnection();

            int code = conn.getResponseCode();
            if (code != 200) {
                Assert.fail("annotation-war fails with response code: " + code);
            } else {
                input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = input.readLine();
                Assert.assertEquals("Invalid return message",
                        "Hello, my servlet, my filter, my listener.", line);
            }
        } catch(Exception ex) {
            Assert.fail("annotation-war fails with exception: " + ex);
        } finally {
            if (input != null) {
                input.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
