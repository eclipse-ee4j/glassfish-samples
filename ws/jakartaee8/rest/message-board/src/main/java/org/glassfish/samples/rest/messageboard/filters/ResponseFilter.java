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
package org.glassfish.samples.rest.messageboard.filters;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

/**
 * {@link ContainerResponseFilter Container response fitler} that adds headers to the response.
 * @author Miroslav Fuksa (miroslav.fuksa at oracle.com)
 */
public class ResponseFilter implements ContainerResponseFilter {
    private final AtomicReference<Date> date = new AtomicReference<Date>();

    @Override
    public void filter(ContainerRequestContext containerRequestContext,
                       ContainerResponseContext containerResponseContext) throws IOException {

        Date currentDate = new Date();
        final Date lastDate = date.getAndSet(currentDate);

        containerResponseContext.getHeaders().add("previous-response", lastDate == null ? "this is the first response"
                : lastDate.toString());
        containerResponseContext.getHeaders().add("this-response", currentDate.toString());
    }
}
