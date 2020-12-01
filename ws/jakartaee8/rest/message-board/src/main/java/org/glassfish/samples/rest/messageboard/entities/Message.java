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

package org.glassfish.samples.rest.messageboard.entities;

import java.util.Date;

public class Message {

    private Date created;
    private String message;
    private int uniqueId;

    public Message(Date created, String message, int uniqueId) {
        this.created = created;
        this.message = message;
        this.uniqueId = uniqueId;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    @Override
    public String toString() {
        return "<span class='created'>CREATED: " + created + "</span> <span class='uniqueId'>ID: "
                + uniqueId + "</span> <span class='message'>MESSAGE: " + message + "</span>";
    }
}
