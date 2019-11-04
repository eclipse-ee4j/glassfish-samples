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

package web.container.customvalve_war;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.glassfish.web.valve.GlassFishValve;

public class TestValve implements GlassFishValve {

    private String valveProperty = null;

    public String getInfo() {
        return getClass().getName();
    }

    public int invoke(Request req, Response resp)
            throws IOException, ServletException {
        req.getRequest().setAttribute("valveProperty", valveProperty);
        return GlassFishValve.INVOKE_NEXT;
    }

    public void postInvoke(Request req, Response resp)
            throws IOException, ServletException {
	// Do nothing
    }

    public void setValveProperty(String value) {
        valveProperty = value;
    }
}
