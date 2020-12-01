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

package ejb.ejb32.war;

import javax.ejb.Stateless;
import javax.ejb.EJB;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Stateless
public class HelloBean {

    @EJB 
    private PropertiesBean propertiesBean;

    @PostConstruct
    private void init() {
	System.out.println("In HelloBean(Stateless)::init()");
    }

    public String sayHello() {
	String message = propertiesBean.getProperty("hello.message");
	return message;
    }

    @PreDestroy
    private void destroy() {
        System.out.println("In HelloBean(Stateless)::destroy()");
    }

}
