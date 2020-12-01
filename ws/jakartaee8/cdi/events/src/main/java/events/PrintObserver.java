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
package events;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Default;

@Singleton
@Startup
public class PrintObserver {
    
    public void onPrintAndBind(@Observes @BindIt PrintEvent event) {
        System.out.println("Printing and binding " + event.getPages() + " pages");
    }
    
    public void onPrint(@Observes @Default PrintEvent event) {
        System.out.println("Printing " + event.getPages() + " pages");
    }
}
