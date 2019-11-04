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
package org.glassfish.samples.twitter.api;

import javax.inject.Named;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Arun Gupta
 */
@Named
@XmlRootElement
public class DirectMessage {
    private String created_at;
    private String sender_screen_name;
    private User sender;
    private String text;
    private String recipient_screen_name;
    private String id;
    private User recipient;
    private String recipient_id;
    private String sender_id;
    private String id_str;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recepient) {
        this.recipient = recepient;
    }

    public String getRecipient_screen_name() {
        return recipient_screen_name;
    }

    public void setRecipient_screen_name(String recepient_screen_name) {
        this.recipient_screen_name = recepient_screen_name;
    }

    public String getRecipient_id() {
        return recipient_id;
    }

    public void setRecipient_id(String recipient_id) {
        this.recipient_id = recipient_id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_screen_name() {
        return sender_screen_name;
    }

    public void setSender_screen_name(String sender_screen_name) {
        this.sender_screen_name = sender_screen_name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }
    
    @Override
    public String toString() {
        return "From " + sender + " to " + recipient;
    }
    
    
}
