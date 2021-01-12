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
package org.glassfish.servlet.non_blocking_io_read_war;

import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;

/**
 * This is the class to illustrate the implementation of ReadListener
 * @author Daniel Guo
 */
public class ReadListenerImpl implements ReadListener {

    private ServletInputStream input;
    private ServletOutputStream output;
    private AsyncContext context;
    private StringBuilder sb = new StringBuilder();

    ReadListenerImpl(ServletInputStream input, ServletOutputStream output, AsyncContext context) {
        this.input = input;
        this.output = output;
        this.context = context;
    }

    /*
     * This method illustrates what ReadListener will do when data 
     * is available to be read.
     */
    @Override
    public void onDataAvailable() throws IOException {
        System.out.println("Data is available");
        while (input.isReady() && !input.isFinished()) {
            sb.append((char) input.read());
        }
        sb.append(" ");
    }

    /*
     * This method illustrates what ReadListender will do 
     * when all the data has been read. 
     */
    @Override
    public void onAllDataRead() throws IOException {
        try {
            output.print("Echo the reverse String from server: " + sb.reverse().toString() + "</br>");
            output.flush();
        } finally {
            context.complete();
        }
        System.out.println("Data is all read");
    }

    /*
     * This method illustrates what ReadListender will do 
     * when error occurs. 
     */
    @Override
    public void onError(Throwable t) {
        context.complete();
        System.out.println("--> onError");
    }
}
