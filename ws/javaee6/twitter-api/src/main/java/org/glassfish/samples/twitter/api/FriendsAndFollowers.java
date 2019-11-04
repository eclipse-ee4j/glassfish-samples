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
 * @author ArunGupta
 */
@Named
@XmlRootElement
public class FriendsAndFollowers {
    private String previous_cursor;
    private long[] ids;
    private String previous_cursor_str;
    private String next_cursor;
    private String next_cursor_str;

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }

    public String getNext_cursor() {
        return next_cursor;
    }

    public void setNext_cursor(String next_cursor) {
        this.next_cursor = next_cursor;
    }

    public String getNext_cursor_str() {
        return next_cursor_str;
    }

    public void setNext_cursor_str(String next_cursor_str) {
        this.next_cursor_str = next_cursor_str;
    }

    public String getPrevious_cursor() {
        return previous_cursor;
    }

    public void setPrevious_cursor(String previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public String getPrevious_cursor_str() {
        return previous_cursor_str;
    }

    public void setPrevious_cursor_str(String previous_cursor_str) {
        this.previous_cursor_str = previous_cursor_str;
    }
    
    
}
