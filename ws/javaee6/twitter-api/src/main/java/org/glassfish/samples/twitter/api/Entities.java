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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Arun Gupta
 */
@XmlRootElement
@Named
public class Entities {
    private UserMentions[] user_mentions;
    private Urls[] urls;
    private Hashtag[] hashtags;
    private Media[] media;

    public Hashtag[] getHashtags() {
        return hashtags;
    }

    public void setHashtags(Hashtag[] hashtags) {
        this.hashtags = hashtags;
    }

    public Urls[] getUrls() {
        return urls;
    }

    public void setUrls(Urls[] urls) {
        this.urls = urls;
    }

    public UserMentions[] getUser_mentions() {
        return user_mentions;
    }

    public void setUser_mentions(UserMentions[] user_mentions) {
        this.user_mentions = user_mentions;
    }

    @XmlElement(nillable=true)
    public Media[] getMedia() {
        return media;
    }

    public void setMedia(Media[] media) {
        this.media = media;
    }
    
    
    
    
}
