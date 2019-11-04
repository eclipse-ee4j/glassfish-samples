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

import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class NullChecker {

    @AroundInvoke
    public Object checkIfNull(InvocationContext ctx)
        throws Exception {
        Method method = ctx.getMethod();
        if (method.getName().equals("initUpperCase")) {
            String param = (String) (ctx.getParameters()[0]);
            if (param == null) {
                // An interceptor can throw any runtime exception or
                // application exceptions that are allowed in the
                // throws clause of the business method
                throw new BadArgumentException("Illegal argument: null");
            }
        }

        // Proceed to the next interceptor
        return ctx.proceed();
    }

}
