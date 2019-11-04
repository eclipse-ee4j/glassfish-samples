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

package org.glassfish.webcomm.sse;

import org.glassfish.webcomm.WebCommunicationProviderImpl;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.glassfish.webcomm.spi.WebCommunicationCdiExtension;

/**
 * ServerSentEventServlet class.
 * @author Santiago.PericasGeertsen@oracle.com
 */
@WebServlet(asyncSupported=true)
public class ServerSentEventServlet extends HttpServlet {

    @Inject
    private WebCommunicationCdiExtension extension;
    
    private WebCommunicationProviderImpl provider;

    @PostConstruct
    public void initialize() {
        provider = (WebCommunicationProviderImpl) extension.getProvider();
        assert provider != null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException 
    {
        ServerSentEventApplicationImpl sseApp =
                provider.getSSEApplication(req.getRequestURI());
        if (sseApp == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.setContentType("text/event-stream");
        AsyncContext ac = req.startAsync(req, resp);
        ac.setTimeout(15*60*1000);      // TODO need config
        ac.addListener(sseApp);
        sseApp.createConnection(ac);
    }
}
