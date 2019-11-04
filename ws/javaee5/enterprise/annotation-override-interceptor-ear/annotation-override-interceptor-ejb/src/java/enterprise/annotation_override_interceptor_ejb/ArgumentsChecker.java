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
package enterprise.annotation_override_interceptor_ejb;

import java.lang.reflect.Method;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class ArgumentsChecker {

    @AroundInvoke
    public Object checkArgument(InvocationContext ctx) throws Exception {

        Map<String, Object> ctxData = ctx.getContextData();
        List<String> interceptorNameList = (List<String>)
            ctxData.get("interceptorNameList");
        if (interceptorNameList == null) {
            interceptorNameList = new ArrayList<String>();
            ctxData.put("interceptorNameList", interceptorNameList);
        }

        //Now add this interceptor name to the list
        interceptorNameList.add("ArgumentsChecker");

        Method method = ctx.getMethod();

        Object objParam  = ctx.getParameters()[0];
        if (! (objParam instanceof String)) {
            throw new BadArgumentException("Illegal argument type: " + objParam);
        }
        String param = (String) (ctx.getParameters()[0]);
        // Note that param cannot be null because
        // it has been validated by the previous (default) interceptor
        char c = param.charAt(0);
        if (!Character.isLetter(c)) {
            // An interceptor can throw any runtime exception or
            // application exceptions that are allowed in the
            // throws clause of the business method
            throw new BadArgumentException("Illegal argument: " + param);
        }

        // Proceed to call next interceptor OR business method
        return ctx.proceed();
    }

}
