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

/**
 *
 * @author arungup
 */
public class SearchResults {
    private float completed_in;
    private long max_id;
    private String max_id_str;
    private String next_page;
    private long page;
    private String query;
    private String refresh_url;
    private SearchResultsTweet[] results;
    private int results_per_page;
    private long since_id;
    private String since_id_str;

    public float getCompleted_in() {
        return completed_in;
    }

    public void setCompleted_in(float completed_in) {
        this.completed_in = completed_in;
    }

    public long getMax_id() {
        return max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    public String getMax_id_str() {
        return max_id_str;
    }

    public void setMax_id_str(String max_id_str) {
        this.max_id_str = max_id_str;
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRefresh_url() {
        return refresh_url;
    }

    public void setRefresh_url(String refresh_url) {
        this.refresh_url = refresh_url;
    }

    public SearchResultsTweet[] getResults() {
        return results;
    }

    public void setResults(SearchResultsTweet[] results) {
        this.results = results;
    }

    public int getResults_per_page() {
        return results_per_page;
    }

    public void setResults_per_page(int results_per_page) {
        this.results_per_page = results_per_page;
    }

    public long getSince_id() {
        return since_id;
    }

    public void setSince_id(long since_id) {
        this.since_id = since_id;
    }

    public String getSince_id_str() {
        return since_id_str;
    }

    public void setSince_id_str(String since_id_str) {
        this.since_id_str = since_id_str;
    }
    
    
    
    
}
