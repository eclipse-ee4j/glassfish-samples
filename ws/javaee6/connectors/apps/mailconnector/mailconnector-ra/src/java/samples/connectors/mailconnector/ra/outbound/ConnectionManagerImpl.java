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

package samples.connectors.mailconnector.ra.outbound;

import java.lang.Object;
import java.util.logging.*;
import javax.resource.spi.*;
import javax.resource.ResourceException;
import java.io.Serializable;

/**
 * The default ConnectionManager implementation for the non-managed scenario.
 * This provides a hook for a resource adapter to pass a connection
 * request to an application server.
 */

public class ConnectionManagerImpl implements ConnectionManager, Serializable 
{
    static Logger logger = 
        Logger.getLogger("samples.connectors.mailconnector.ra.outbound");

    public ConnectionManagerImpl() 
    {
	logger.fine("In ConnectionManagerImpl");
    }

    /**
     * The method allocateConnection is called by the resource adapter's 
     * connection factory instance. This lets the connection factory instance 
     * provided by the resource adapter pass a connection request to the 
     * ConnectionManager instance. The connectionRequestInfo parameter 
     * represents information specific to the resource adapter for handling 
     * the connection request.
     *
     * @param mcf	used by application server to delegate connection 
     *                  matching/creation
     * @param cxRequestInfo  connection request information
     *
     * @return  connection handle with an EIS specific connection interface
     *
     * @exception ResourceException if an error occurs
     */

    public Object allocateConnection(ManagedConnectionFactory mcf, 
				     ConnectionRequestInfo cxRequestInfo) 
	throws ResourceException
    {
        ManagedConnection mc = mcf.createManagedConnection(null, cxRequestInfo);
        return mc.getConnection(null, cxRequestInfo);
    }
}
