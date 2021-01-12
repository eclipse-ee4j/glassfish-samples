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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.glassfish.servlet.trailer_war;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class is a client for generating and receving HTTP trailer.
 *
 * @author Shing Wai Chan
 */
@WebServlet("")
public class ClientServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        StringBuilder sb = new StringBuilder();

        String hostStr = req.getServerName();
        int port = req.getServerPort();

        try (
            Socket socket = new Socket(hostStr, port);
            OutputStream output = socket.getOutputStream();
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input))
        ) {
            String reqStr = (new StringBuffer("POST /trailer-war/test HTTP/1.1\r\n")).
                append("Host: " + hostStr + "\r\n").
                append("Transfer-encoding: chunked\r\n").
                append("Connection: close\r\n").
                append("trailer: foo\r\n").
                append("\r\n").
                append("5\r\n").
                append("hello\r\n").
                append("0\r\n").
                append("foo: A\r\n").
                append("\r\n").
                toString();

            output.write(reqStr.getBytes(Charset.forName("US-ASCII")));

            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("bar")) {
                    sb.append(line).append("\r\n");
                }
            }
        }

        res.getWriter().write(sb.toString());
    }
}
