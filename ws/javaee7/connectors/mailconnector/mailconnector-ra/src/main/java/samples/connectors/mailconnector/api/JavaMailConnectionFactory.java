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

package samples.connectors.mailconnector.api;

import javax.mail.*;
import javax.mail.Message.*;
import javax.mail.internet.*;
import javax.resource.ResourceException;
import javax.resource.cci.ConnectionSpec;


/**
 * Interface for obtaining mail server connections. 
 * 
 * @author Alejandro E. Murillo
 */

public interface JavaMailConnectionFactory
{
    /**
     * Gets a connection to the Mail Server.
     * Passes along mail server and user info.
     *
     * @return Connection	Connection instance
     */

    public JavaMailConnection createConnection()
        throws ResourceException;

    /**
     * Gets a connection to a Mail Server instance. A component should use 
     * the getConnection variant with a javax.resource.cci.ConnectionSpec 
     * parameter if it needs to pass any resource-adapter-specific security 
     * information and connection parameters.
     *
     * @param properties  connection parameters and security information 
     *                    specified as ConnectionSpec instance
     * @return  a JavaMailConnection instance
     */

    public JavaMailConnection createConnection(ConnectionSpec properties)
        throws ResourceException;
}
