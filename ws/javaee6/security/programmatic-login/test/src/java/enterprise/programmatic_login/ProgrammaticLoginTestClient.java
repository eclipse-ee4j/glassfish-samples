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

package enterprise.programmatic_login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import junit.framework.Assert;
import org.junit.Test;

public class ProgrammaticLoginTestClient {

    static String host = System.getProperty("http.host", "localhost");
    static String portS = System.getProperty("http.port", "8080");
    static final String url = "/programmatic-login/loginservlet";
    static final String userName = "txtUserName=javaee6user";
    static final String password = "&txtPassword=abc123";
    

    @Test
    public void loginWithCredentials()
            throws Exception {
        String endPoint = "http://" + host + ":" + portS + url;
        String requestParams = userName + password;
        StringBuilder response = sendGetRequest(endPoint, requestParams);
        System.out.println(response);
        int fromIndex = response.indexOf("After Login...");
        int secondFromIndex = response.indexOf("IsUserInRole?..",fromIndex);
        String isUserInRoleVal = response.substring(secondFromIndex+15,secondFromIndex+20);
        Assert.assertTrue(isUserInRoleVal != null && isUserInRoleVal.contains("true"));
    }

    private static StringBuilder sendGetRequest(String endpoint, String requestParameters) {
        String result = null;

        StringBuilder data = new StringBuilder();
        try {


            String urlStr = endpoint;
            if (requestParameters != null && requestParameters.length() > 0) {
                urlStr += "?" + requestParameters;
            }
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                data.append(line);
            }
            rd.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

   @Test
    public  void loginWithoutCredentials()
    throws Exception {
        String endPoint = "http://" + host + ":" + portS + url;
        String requestParams = userName + password;
        StringBuilder response = sendGetRequest(endPoint, null);
        System.out.println(response);
        int fromIndex = response.indexOf("After Login...");
        int secondFromIndex = response.indexOf("IsUserInRole?..",fromIndex);
        String isUserInRoleVal = response.substring(secondFromIndex+15,secondFromIndex+20);
        Assert.assertTrue(isUserInRoleVal != null && isUserInRoleVal.contains("false"));
    
    }
}
