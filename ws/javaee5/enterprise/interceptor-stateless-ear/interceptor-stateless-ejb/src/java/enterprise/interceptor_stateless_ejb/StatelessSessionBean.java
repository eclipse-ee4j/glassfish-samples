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
package enterprise.interceptor_stateless_ejb;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

// This bean uses two interceptors to validate
// the input to its (only) business method.
// Note that a single interceptor would suffice
// but to demonstrate the use of interceptor
// chaining, we use two interceptors

@Stateless
@Interceptors( { NullChecker.class, ArgumentsChecker.class })
public class StatelessSessionBean
    implements StatelessSession {

    // This business method is called after
    // the above two interceptor's @AroundInvoke
    // methods are invoked. Hence it is guaranteed
    // that the argument to this method is not null
    // and it starts with a letter
    public String initUpperCase(String val) {
        String first = val.substring(0, 1);
        return first.toUpperCase() + val.substring(1);
    }

}
