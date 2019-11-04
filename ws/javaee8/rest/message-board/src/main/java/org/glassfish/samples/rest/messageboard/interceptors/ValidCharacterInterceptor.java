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
package org.glassfish.samples.rest.messageboard.interceptors;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;

/**
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */
public class ValidCharacterInterceptor implements ReaderInterceptor {

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext readerInterceptorContext) throws IOException, WebApplicationException {
        final InputStream originalInputStream = readerInterceptorContext.getInputStream();
        readerInterceptorContext.setInputStream(new InputStream() {

            @Override
            public int read() throws IOException {
                boolean isOk;
                int b;
                do {
                    b = originalInputStream.read();
                    isOk = b == -1 || Character.isLetterOrDigit(b) || Character.isWhitespace(b) || b == ((int) '.');
                } while (!isOk);

                return b;
            }
        });
        try {
            return readerInterceptorContext.proceed();
        } finally {
            readerInterceptorContext.setInputStream(originalInputStream);
        }
    }
}
