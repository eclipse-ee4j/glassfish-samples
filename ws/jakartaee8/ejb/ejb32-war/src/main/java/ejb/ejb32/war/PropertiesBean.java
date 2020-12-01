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

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.EJBException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.Properties;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
@Startup
public class PropertiesBean {

    private Properties props;
    private int accessCount = 0;

    @PostConstruct
    private void startup() {

	System.out.println("In PropertiesBean(Singleton)::startup()");

	try {

            InputStream propsStream = 
		PropertiesBean.class.getResourceAsStream("/app.properties");
            props = new Properties();
            props.load(propsStream);

        } catch(Exception e) {
	    throw new EJBException("PropertiesBean initialization error", e);
        }

    }

    public String getProperty(String name) {
	accessCount++;
	return props.getProperty(name);
    }

    public int getAccessCount() {
	return accessCount;
    }

    @PreDestroy
    private void shutdown() {
        System.out.println("In PropertiesBean(Singleton)::shutdown()");
    }

}
