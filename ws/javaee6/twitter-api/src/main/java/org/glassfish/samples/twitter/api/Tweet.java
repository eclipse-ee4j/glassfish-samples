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
public class Tweet {
    private Entities entities;
    
//    @XmlElement(nillable=true)
    private String in_reply_to_user_id;
    
    private boolean favorited;
    
//    @XmlElement(nillable=true)
    private Place place;
    
//    @XmlElement(nillable=true)
    private Coordinates coordinates;
    
    private String retweet_count;
    
//    @XmlElement(nillable=true)
    private String in_reply_to_screen_name;
    
//    @XmlElement(nillable=true)
    private String in_reply_to_status_id_str;
    
    private boolean possibly_sensitive;
    
    private boolean possibly_sensitive_editable;
    
//    @XmlElement(nillable=true)
    private Geo geo;
    
    private boolean retweeted;
    
//    @XmlElement(nillable=true)
    private String in_reply_to_user_id_str;
    
    private User user;
    
    private String created_at;
    
//    @XmlElement(nillable=true)
    private String in_reply_to_status_id;
    
    private String id_str;
    
    private int[] contributors;
    
    private String source;
    
    private boolean truncated;
    
//    @XmlElement(nillable=true)
    private String annotations;
    
    private long id;
    
//    @XmlElement(nillable=true)
    private Tweet retweeted_status;
    
    private String text;
    
    @XmlElement(nillable=true)
    public int[] getContributors() {
        return contributors;
    }

    public void setContributors(int[] contributors) {
        this.contributors = contributors;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public Geo getGeo() {
        return geo;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_user_id_str() {
        return in_reply_to_user_id_str;
    }

    public void setIn_reply_to_user_id_str(String in_reply_to_user_id_str) {
        this.in_reply_to_user_id_str = in_reply_to_user_id_str;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public boolean isPossibly_sensitive() {
        return possibly_sensitive;
    }

    public void setPossibly_sensitive(boolean possibly_sensitive) {
        this.possibly_sensitive = possibly_sensitive;
    }

    public String getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(String retweet_count) {
        this.retweet_count = retweet_count;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public Tweet getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Tweet retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @XmlElement(nillable=true)
    public boolean isPossibly_sensitive_editable() {
        return possibly_sensitive_editable;
    }

    public void setPossibly_sensitive_editable(boolean possibly_sensitive_editable) {
        this.possibly_sensitive_editable = possibly_sensitive_editable;
    }

    public String getAnnotations() {
        return annotations;
    }

    public void setAnnotations(String annotations) {
        this.annotations = annotations;
    }

    @Override
    public String toString() {
        return "From " + user + ": " + text.substring(0, 40);
    }
}
