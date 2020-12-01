/*
 	Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
	
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

import jakarta.resource.spi.*;
import jakarta.resource.ResourceException;
import java.lang.String;
import jakarta.resource.spi.IllegalStateException;
import java.util.*;
import java.util.logging.*;

/**
 * The ManagedConnectionMetaData interface provides information about the
 * underlying EIS instance associated with a ManagedConnection instance. An
 * application server uses this information to get runtime information about a
 * connected EIS instance.
 */

public class ManagedConnectionMetaDataImpl implements ManagedConnectionMetaData {
    private ManagedConnectionImpl mc;

    static Logger                 logger   = Logger.getLogger(
                                                   "samples.connectors.mailconnector.ra.outbound",
                                                   "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    ResourceBundle                resource = java.util.ResourceBundle
                                                   .getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     * 
     * @param mc
     *            the managed connection that created this instance of
     *            ManagedConnectionMetaData
     */

    public ManagedConnectionMetaDataImpl(ManagedConnectionImpl mc) {
        logger.info("ManagedConnectionMetaDataImpl::Constructor");
        this.mc = mc;
    }

    /**
     * Returns the product name of the underlying EIS instance connected through
     * the ManagedConnection.
     * 
     * @return product name of the EIS instance
     */

    public String getEISProductName() throws ResourceException {
        String productName = null;

        // ToDo: Add service specific code here

        return productName;
    }

    /**
     * Returns the product version of the underlying EIS instance connected
     * through the ManagedConnection.
     * 
     * @return product version of the EIS instance
     */

    public String getEISProductVersion() throws ResourceException {
        String productVersion = null;
        // ToDo: Add service specific code here

        return productVersion;
    }

    /**
     * Returns the maximum number of active concurrent connections that an EIS
     * instance can support across client processes. If an EIS instance does not
     * know about (or does not have) any such limit, it returns zero.
     * 
     * @return maximum number of active concurrent connections
     */

    public int getMaxConnections() throws ResourceException {
        int maxConnections = 0;

        // ToDo: Add service specific code here

        return maxConnections;
    }

    /**
     * Returns the name of the user associated with the ManagedConnection
     * instance. The name corresponds to the resource principal under whose
     * security context a connection to the EIS instance has been established.
     * 
     * @return name of the user
     */

    public String getUserName() throws ResourceException {
        if (mc.isDestroyed()) {
            throw new IllegalStateException(
                    resource.getString("DESTROYED_CONNECTION"));
        }
        return mc.getUserName();
    }
}
