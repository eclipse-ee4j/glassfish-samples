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
package org.glassfish.samples.rest.messageboard.resources;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.samples.rest.messageboard.entities.Message;
import org.glassfish.samples.rest.messageboard.entities.MessageListWriter;
import org.glassfish.samples.rest.messageboard.entities.MessageWriter;
import org.glassfish.samples.rest.messageboard.exceptions.NotFoundExceptionMapper;
import org.glassfish.samples.rest.messageboard.filters.ResponseFilter;
import org.glassfish.samples.rest.messageboard.interceptors.ValidCharacterInterceptor;

@ApplicationPath("/app")
public class JaxRsApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        // register root resources/providers
        classes.add(MessageBoardRootResource.class);
        classes.add(MessageHolderSingletonBean.class);
        classes.add(NotFoundExceptionMapper.class);
        classes.add(MessageWriter.class);
        classes.add(MessageListWriter.class);
        classes.add(Message.class);
        classes.add(ResponseFilter.class);
        classes.add(ValidCharacterInterceptor.class);
        return classes;
    }
}
