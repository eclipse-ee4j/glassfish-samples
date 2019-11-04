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

package enterprise.messageboard.entities;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Stateless
@Provider
public class MessageListWriter implements MessageBodyWriter<List<Message>> {

    @Context UriInfo ui;

    public boolean isWriteable(Class<?> clazz, Type type, Annotation[] annotation, MediaType mediaType) {
        return verifyGenericType(type);
    }

    private boolean verifyGenericType(Type genericType) {
        if (!(genericType instanceof ParameterizedType)) return false;

        final ParameterizedType pt = (ParameterizedType)genericType;

        if (pt.getActualTypeArguments().length > 1) return false;

        if (!(pt.getActualTypeArguments()[0] instanceof Class)) return false;

        final Class listClass = (Class)pt.getActualTypeArguments()[0];
        return listClass == Message.class;
    }

    public long getSize(List<Message> messages, Class<?> clazz, Type type, Annotation[] annotation, MediaType mediaType) {
        return -1;
    }

    public void writeTo(List<Message> messages, Class<?> clazz, Type type, Annotation[] annotation, MediaType mediaType, MultivaluedMap<String, Object> arg5, OutputStream ostream) throws IOException, WebApplicationException {
        for (Message m : messages) {
            ostream.write(m.toString().getBytes());
            URI mUri = ui.getAbsolutePathBuilder().path(Integer.toString(m.getUniqueId())).build();
            ostream.write((" <a href='" + mUri.toASCIIString() + "'>link</a><br />").getBytes());
        }
    }
}
