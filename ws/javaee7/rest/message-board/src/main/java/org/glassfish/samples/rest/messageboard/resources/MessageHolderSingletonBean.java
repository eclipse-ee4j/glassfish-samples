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

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Singleton;

import org.glassfish.samples.rest.messageboard.entities.Message;

@Singleton
public class MessageHolderSingletonBean {

    private LinkedList<Message> list = new LinkedList<Message>();
    private int maxMessages = 10;

    int currentId = 0;

    public MessageHolderSingletonBean() {
        // initial content
        addMessage("msg0", new Date(0));
        addMessage("msg1", new Date(1000));
        addMessage("msg2", new Date(2000));
    }

    public List<Message> getMessages() {
        List<Message> l = new LinkedList<Message>();

        int index = 0;

        while (index < list.size() && index < maxMessages) {
            l.add(list.get(index));
            index++;
        }

        return l;
    }

    private synchronized int getNewId() {
        return currentId++;
    }


    public synchronized Message addMessage(String msg) {
        return addMessage(msg, new Date());
    }

    private synchronized Message addMessage(String msg, Date date) {
        Message m = new Message(date, msg, getNewId());

        list.addFirst(m);

        return m;
    }

    public Message getMessage(int uniqueId) {
        int index = 0;
        Message m;

        while (index < list.size()) {
            if ((m = list.get(index)).getUniqueId() == uniqueId)
                return m;
            index++;
        }

        return null;
    }

    public synchronized boolean deleteMessage(int uniqueId) {
        int index = 0;

        while (index < list.size()) {
            if (list.get(index).getUniqueId() == uniqueId) {
                list.remove(index);
                return true;
            }
            index++;
        }

        return false;
    }
}
