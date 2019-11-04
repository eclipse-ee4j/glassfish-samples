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

package jsf2.demo.scrum.web.event;

import javax.faces.event.SystemEvent;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
public class CurrentStoryChangeEvent extends SystemEvent {
    private static final long serialVersionUID = -6484414742384961535L;

    public CurrentStoryChangeEvent(Object source) {
        super(source);
    }
}
