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

import javax.inject.Named;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Arun Gupta
 */
@XmlRootElement
@Named
public class Metadata {
    private String result_type;
    private int recent_retweets;

    public String getResult_type() {
        return result_type;
    }

    public void setResult_type(String result_type) {
        this.result_type = result_type;
    }

    public int getRecent_retweets() {
        return recent_retweets;
    }

    public void setRecent_retweets(int recent_retweets) {
        this.recent_retweets = recent_retweets;
    }
    
}
