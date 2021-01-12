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

import jakarta.resource.cci.*;
import jakarta.resource.ResourceException;
import java.lang.String;
import java.util.*;
import java.util.logging.*;

/**
 * This class provides information about an EIS instance connected through a
 * Connection instance.
 */

public class ConnectionMetaDataImpl implements ConnectionMetaData {

    private ManagedConnectionImpl mc;

    static Logger                 logger   = Logger.getLogger(
                                                   "samples.connectors.mailconnector.ra.outbound",
                                                   "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    ResourceBundle                resource = ResourceBundle
                                                   .getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     * 
     * @param mc
     *            the physical connection of the JavaMailConnection that created
     *            this instance of ConnectionMetaDataImpl
     */

    public ConnectionMetaDataImpl(ManagedConnectionImpl mc) {
        logger.fine("ConnectionMetaDataImpl::constructor");
        this.mc = mc;
    }

    /**
     * Returns product name of the underlying EIS instance connected through the
     * Connection that produced this metadata.
     * 
     * @return product name of the EIS instance
     */

    public String getEISProductName() throws ResourceException {
        String productName = "JavaMail Connector";
        return productName;
    }

    /**
     * Returns product version of the underlying EIS instance.
     * 
     * @return product version of the EIS instance
     */

    public String getEISProductVersion() throws ResourceException {
        String productVersion = "0.1";
        return productVersion;
    }

    /**
     * Returns the user name for an active connection known to the Mail Server.
     * 
     * @return String representing the user name
     */

    public String getUserName() throws ResourceException {

        if (mc.isDestroyed()) {
            throw new ResourceException(
                    resource.getString("DESTROYED_CONNECTION"));
        }
        return mc.getUserName();
    }

    // Could return other connection info (serverName, etc.)
}
