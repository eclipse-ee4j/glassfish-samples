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
@XmlRootElement
@Named
public class Sizes {
    private Size large;
    private Size medium;
    private Size thumb;
    private Size small;

    public Size getLarge() {
        return large;
    }

    public void setLarge(Size large) {
        this.large = large;
    }

    public Size getMedium() {
        return medium;
    }

    public void setMedium(Size medium) {
        this.medium = medium;
    }

    public Size getSmall() {
        return small;
    }

    public void setSmall(Size small) {
        this.small = small;
    }

    public Size getThumb() {
        return thumb;
    }

    public void setThumb(Size thumb) {
        this.thumb = thumb;
    }
    
    
}
