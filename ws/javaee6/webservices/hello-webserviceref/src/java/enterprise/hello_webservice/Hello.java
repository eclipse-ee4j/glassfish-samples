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

package enterprise.hello_webservice;

import javax.jws.WebService;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.WebServiceException;

@MTOM
@WebService
public class Hello
{
    public String getHello(String name)
    {
        return "Hello " + name + "!";
    }

    public byte[] getHelloBinary(byte[] name) {
        try {
        String nameStr = new String(name,"UTF-8");        
        return ("Hello " + nameStr + "!").getBytes("UTF-8");
        } catch(java.io.UnsupportedEncodingException ex) {
          throw new WebServiceException(ex);
        }

    }
}


