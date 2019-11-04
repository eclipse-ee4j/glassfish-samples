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

package enterprise.messageboard.resources;

import enterprise.messageboard.entities.Message;
import enterprise.messageboard.exceptions.NotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Stateless
public class MessageBoardResourceBean {

    @Context private UriInfo ui;
    @EJB MessageHolderSingletonBean singleton;

    @GET
    public List<Message> getMessages() {
        return singleton.getMessages();
    }

    @POST
    public Response addMessage(String msg) throws URISyntaxException {
        Message m = singleton.addMessage(msg);

        URI msgURI = ui.getRequestUriBuilder().path(Integer.toString(m.getUniqueId())).build();

        return Response.created(msgURI).build();
    }

    @Path("{msgNum}")
    @GET
    public Message getMessage(@PathParam("msgNum") int msgNum) throws NotFoundException {
        Message m = singleton.getMessage(msgNum);

        if(m == null)
            throw new NotFoundException();

        return m;

    }

    @Path("{msgNum}")
    @DELETE
    public void deleteMessage(@PathParam("msgNum") int msgNum) throws NotFoundException {
        boolean deleted = singleton.deleteMessage(msgNum);

        if(!deleted)
            throw new NotFoundException();
    }
}





