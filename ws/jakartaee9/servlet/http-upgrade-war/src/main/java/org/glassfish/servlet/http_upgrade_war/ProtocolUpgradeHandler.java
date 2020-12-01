/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.servlet.http_upgrade_war;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.WebConnection;

/**
 * This class illustrates the implementation of HTTPUpgradeHandler
 * @author Daniel
 */
public class ProtocolUpgradeHandler implements HttpUpgradeHandler {

    private static final String CRLF = "\r\n";
    private static final byte[] echoData = new byte[128];
    private WebConnection wc = null;

    @Override
    public void init(WebConnection wc) {
        this.wc = wc;
        try {

            ServletOutputStream output = wc.getOutputStream();
            ServletInputStream input = wc.getInputStream();
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            // Reading the data into byte array
            input.read(echoData);

            // Setting new protocol header 
            String resStr = "Dummy Protocol/1.0 " + CRLF;
            resStr += "Server: Glassfish/ServerTest" + CRLF;
            resStr += "Content-Type: text/html" + CRLF;
            resStr += "Connection: Upgrade" + CRLF;
            resStr += "Date: " + dateFormat.format(calendar.getTime()) + CRLF;
            resStr += CRLF;
            // Appending data with new protocol
            resStr += new String(echoData) + CRLF;
            
            // Sending back to client
            System.out.println("Echo data is: " + new String(echoData));
            output.write(resStr.getBytes());
            output.flush();

        } catch (IOException ex) {
            Logger.getLogger(ProtocolUpgradeHandler.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void destroy() {
        try {
            wc.close();
        } catch (Exception ex) {
            Logger.getLogger(ProtocolUpgradeHandler.class.getName()).log(Level.SEVERE, 
                    "Failed to close connection", ex);
        }
    }
}
