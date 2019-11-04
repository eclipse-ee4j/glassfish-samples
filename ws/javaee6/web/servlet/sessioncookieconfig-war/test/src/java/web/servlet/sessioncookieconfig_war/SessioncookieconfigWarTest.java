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

package web.servlet.sessioncookieconfig_war;

import java.net.*;
import org.junit.*;

/**
 * junit test for sessioncookieconfig-war.
 *
 * @author Jan Luehe
 */
public class SessioncookieconfigWarTest {

    private String host = null;
    private int port = 8080;

    @Before
    public void setUpClass() throws Exception {
        host = System.getProperty("javaee.server.name");
        port = Integer.parseInt(System.getProperty("javaee.server.port"));
    }

    @Test
    public void testWebClient() {
        String url = "http://" + host + ":" + port + "/sessioncookieconfig-war/";
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)(new URL(url)).openConnection();
            int code = conn.getResponseCode();
            if (code != 200) {
                Assert.fail(
                    "sessioncookieconfig-war failed with response code: " +
                    code);
            }
        } catch(Exception ex) {
            Assert.fail("sessioncookieconfig-war failed with exception: " +
                        ex);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
