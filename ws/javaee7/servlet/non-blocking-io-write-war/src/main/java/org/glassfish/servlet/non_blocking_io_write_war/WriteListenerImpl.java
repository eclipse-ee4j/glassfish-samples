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
package org.glassfish.servlet.non_blocking_io_write_war;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 * This is the class to illustrate the implementation of WriteListener
 *
 * @author Daniel Guo
 */
public class WriteListenerImpl implements WriteListener {

    private ServletOutputStream output;
    private AsyncContext context;
    private final int LENGTH = 587952;
    private int count = 0;
    private byte[] CRLF = "\r\n".getBytes();
    private final long startTime = System.currentTimeMillis();

    WriteListenerImpl(ServletOutputStream output, AsyncContext context) {
        this.context = context;
        this.output = output;
    }

    /*
     * This method illustrates what WriteListener will do when the data
     * is available to be written
     */
    @Override
    public void onWritePossible() throws IOException{

        while (output.isReady()) {
            // Set a timeout for WriteListener
            if (System.currentTimeMillis() - startTime > 10000 || count > 10) {
                output.println("---> Time out");
                throw new IOException("Time out");
            }

            System.out.println("Writing data...... ");
            writeBytes(output, 'a');
            count++;
        }

        System.out.println("Output writer is not ready");
        output.flush();

    }

    @Override
    public void onError(Throwable t) {
        context.complete();
        System.out.println("--> onError");
    }

    protected void writeBytes(ServletOutputStream output, char data) throws IOException {
        byte[] b = new byte[LENGTH];
        Arrays.fill(b, 0, LENGTH, (byte) data);
        output.write(b);
        output.write(CRLF);
    }
}
