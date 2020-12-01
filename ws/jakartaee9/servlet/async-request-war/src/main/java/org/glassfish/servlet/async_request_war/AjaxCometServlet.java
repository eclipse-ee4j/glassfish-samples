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

package org.glassfish.servlet.async_request_war;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * This class illustrates the usage of Servlet 3.0 asynchronization APIs.
 * It is ported from Grizzly Comet sample and use Servlet 3.0 API here.
 * @author Shing Wai Chan
 * @author JeanFrancois Arcand
 */
@WebServlet(urlPatterns = {"/chat"}, asyncSupported = true)
public class AjaxCometServlet extends HttpServlet {

    private static final Queue<AsyncContext> queue = new ConcurrentLinkedQueue<AsyncContext>();

    private static final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<String>();

    private static final String BEGIN_SCRIPT_TAG = "<script type='text/javascript'>\n";

    private static final String END_SCRIPT_TAG = "</script>\n";

    private static final long serialVersionUID = -2919167206889576860L;

    private static final String JUNK = "<!-- Comet is a programming technique that enables web " +
            "servers to send data to the client without having any need " +
            "for the client to request it. -->\n";

    private Thread notifierThread = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Runnable notifierRunnable = new Runnable() {
            public void run() {
                boolean done = false;
                while (!done) {
                    String cMessage = null;
                    try {
                        cMessage = messageQueue.take();
                        for (AsyncContext ac : queue) {
                            try {
                                PrintWriter acWriter = ac.getResponse().getWriter();
                                acWriter.println(cMessage);
                                acWriter.flush();
                            } catch(IOException ex) {
                                System.out.println(ex);
                                queue.remove(ac);
                            }
                        }
                    } catch(InterruptedException iex) {
                        done = true;
                        System.out.println(iex);
                    }
                }
            }
        };
        notifierThread = new Thread(notifierRunnable);
        notifierThread.start();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        res.setHeader("Cache-Control", "private");
        res.setHeader("Pragma", "no-cache");
        
        PrintWriter writer = res.getWriter();
        // for Safari, Chrome, IE and Opera
        for (int i = 0; i < 10; i++) {
            writer.write(JUNK);
        }
        writer.flush();

        final AsyncContext ac = req.startAsync();
        ac.setTimeout(10  * 60 * 1000);
        ac.addListener(new AsyncListener() {
            public void onComplete(AsyncEvent event) throws IOException {
                queue.remove(ac);
            }

            public void onTimeout(AsyncEvent event) throws IOException {
                queue.remove(ac);
            }

            public void onError(AsyncEvent event) throws IOException {
                queue.remove(ac);
            }

            public void onStartAsync(AsyncEvent event) throws IOException {
            }
        });
        queue.add(ac);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/plain");
        res.setHeader("Cache-Control", "private");
        res.setHeader("Pragma", "no-cache");

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String name = req.getParameter("name");
      
        
        if ("login".equals(action)) {
            String cMessage = BEGIN_SCRIPT_TAG + toJsonp("System Message", name + " has joined.") + END_SCRIPT_TAG;
            notify(cMessage);

            res.getWriter().println("success");
        } else if ("post".equals(action)) {
            String message = req.getParameter("message");
            String cMessage = BEGIN_SCRIPT_TAG + toJsonp(name, message) + END_SCRIPT_TAG;
            notify(cMessage);

            res.getWriter().println("success");
        } else {
            res.sendError(422, "Unprocessable Entity");
        }
    }

    @Override
    public void destroy() {
        queue.clear();
        notifierThread.interrupt();
    }

    private void notify(String cMessage) throws IOException {
        try {
            messageQueue.put(cMessage);
        } catch(Exception ex) {
            IOException t = new IOException();
            t.initCause(ex);
            throw t;
        }
    }

    private String escape(String orig) {
        StringBuffer buffer = new StringBuffer(orig.length());

        for (int i = 0; i < orig.length(); i++) {
            char c = orig.charAt(i);
            switch (c) {
            case '\b':
                buffer.append("\\b");
                break;
            case '\f':
                buffer.append("\\f");
                break;
            case '\n':
                buffer.append("<br />");
                break;
            case '\r':
                // ignore
                break;
            case '\t':
                buffer.append("\\t");
                break;
            case '\'':
                buffer.append("\\'");
                break;
            case '\"':
                buffer.append("\\\"");
                break;
            case '\\':
                buffer.append("\\\\");
                break;
            case '<':
                buffer.append("&lt;");
                break;
            case '>':
                buffer.append("&gt;");
                break;
            case '&':
                buffer.append("&amp;");
                break;
            default:
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    private String toJsonp(String name, String message) {
        return "window.parent.app.update({ name: \"" + escape(name) + "\", message: \"" + escape(message) + "\" });\n";
    }
}
