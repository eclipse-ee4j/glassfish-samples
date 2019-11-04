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

package enterprise.web_security_annotation_client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import junit.framework.Assert;
import org.junit.Test;


public class WebSecurityAnnotationTest {

    static String host = System.getProperty("javaee.server.name","localhost");
    static String portS = System.getProperty("javaee.server.port","8080");
    static int port = new Integer(portS).intValue();
    static final String url = "/web-security-annotation/annotate";
    static final String auth = "Authorization: 	Basic amF2YWVlNnVzZXI6YWJjMTIz\n";


    /**
     * Connect to host:port and issue GET with given auth info.
     *
     */
    @Test
    public  void goGetTest()
            throws Exception {
        Socket s = new Socket(host, port);
        OutputStream os = s.getOutputStream();

        os.write(("GET "+url+"\n").getBytes());
        os.write(auth.getBytes());
        os.write("\n".getBytes());

        InputStream is = s.getInputStream();
        BufferedReader bis = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuilder outPut = new StringBuilder();

        while ((line = bis.readLine()) != null) {
            outPut.append(line);
           System.out.println(line);
        }
        
        int index = outPut.indexOf("Welcome");
        
        Assert.assertTrue(index != -1);
        

        s.close();
        
    }
    /**
     * Connect to host:port and issue GET with given auth info.
     *
     */
    @Test
    public  void goPostTest()
            throws Exception {
        Socket s = new Socket(host, port);
        OutputStream os = s.getOutputStream();

        os.write(("POST "+url+"\n").getBytes());
        os.write(auth.getBytes());
        os.write("\n".getBytes());

        InputStream is = s.getInputStream();
        BufferedReader bis = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuilder outPut = new StringBuilder();

        while ((line = bis.readLine()) != null) {
             outPut.append(line);
           System.out.println(line);
        }
        int index = outPut.indexOf("403");
        
        Assert.assertTrue(index != -1);

        s.close();
        
    }
}
