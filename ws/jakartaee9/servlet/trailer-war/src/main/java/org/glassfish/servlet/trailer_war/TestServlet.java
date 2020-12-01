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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class illustrates HTTP trailer API in Servlet.
 *
 * @author Shing Wai Chan
 */
@WebServlet("/test")
public class TestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/plain");
        res.addHeader("Transfer-encoding", "chunked");
        res.addHeader("TE", "trailers");
        res.addHeader("Trailer", "bar");

        StringBuilder sb = new StringBuilder();

        final InputStream in = req.getInputStream();
        int b;
        while ((b = in.read()) != -1) {
            sb.append((char) b);
        }

        String foo = null;
        int size = -1;

        if (req.isTrailerFieldsReady()) {
            Map<String, String> reqTrailerFields = req.getTrailerFields();
            size = reqTrailerFields.size();
            foo = reqTrailerFields.get("foo");
        }

        final String finalFoo = foo;
        final int finalSize = size;
        res.setTrailerFields(new Supplier<Map<String, String>>() {
            @Override
            public Map<String, String> get() {
                Map<String, String> map = new HashMap<>();
                map.put("bar", finalFoo + finalSize);
                return map;
            }
        });
        res.getWriter().write(sb.toString());
    }
}
