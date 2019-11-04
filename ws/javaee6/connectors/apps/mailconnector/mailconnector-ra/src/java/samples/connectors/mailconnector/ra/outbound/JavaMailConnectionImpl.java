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

import javax.resource.cci.*;
import javax.resource.ResourceException;
import javax.resource.NotSupportedException;
import javax.resource.spi.ConnectionEvent;
import java.util.*;
import java.util.logging.*;
import javax.mail.*;

import samples.connectors.mailconnector.api.*;
import samples.connectors.mailconnector.share.*;


/**
 * Application-level connection handle that is used by a 
 * client component to access an EIS instance.
 */

public class JavaMailConnectionImpl implements JavaMailConnection
{

    private ManagedConnectionImpl mc; // if mc is null, connection is invalid
    private javax.mail.Folder 		folder; // Folder 1x1 with connection

    static Logger logger = 
        Logger.getLogger("samples.connectors.mailconnector.ra.outbound",
            "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    ResourceBundle resource = 
        java.util.ResourceBundle.getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Constructor.
     *
     * @param mc  a physical connection to an underlying EIS
     * @param connectionInfo  connection-specific info/properties 
     * 
     */
  
    JavaMailConnectionImpl(ManagedConnectionImpl mc, 
                           javax.mail.Folder folder) 
    {
        this.mc = mc;
        this.folder = folder;
	logger.fine(" 5. JavaMailConnectionImpl::Constructor");
    }
  
    

    /**
     * Retrieves a ManagedConnection.
     *
     *	@return  a ManagedConnection instance representing the physical 
     *           connection to the EIS
     */

    public ManagedConnectionImpl getManagedConnection() 
    {
	logger.finest(" -- In JavaMailConnectionImpl::getManagedConnection mc=" + mc);
        return mc;
    }

    /**
     * Returns a javax.resource.cci.LocalTransaction instance that enables a 
     * component to demarcate resource manager local transactions on the 
     * Connection.
     *
     * Because this implementation does not support transactions, the method
     * throws an exception.
     *
     * @return  a LocalTransaction instance
     */
    public javax.resource.cci.LocalTransaction getLocalTransaction() 
	throws ResourceException
    {
        throw new ResourceException(resource.getString("NO_TRANSACTION"));
    }

    
    /**
     * Returns the metadata for the ManagedConnection.
     *
     * @return  a ConnectionMetaData instance
     */
    public ConnectionMetaData getMetaData() 
	throws ResourceException
    {
	logger.finest(" -- In JavaMailConnectionImpl:: getMetaData mc=" + mc);
        return new ConnectionMetaDataImpl(mc);
    }

    /**
     * Closes the connection.
     */
    public void close() 
	throws ResourceException
    {
 	logger.finest(" -- In JavaMailConnectionImpl:: close mc=" + mc);
        if (mc == null) 
	   return;  // connection is already closed
        mc.removeJavaMailConnection(this);

	// Send a close event to the App Server
        mc.sendEvent(ConnectionEvent.CONNECTION_CLOSED, null, this);
        mc = null;
    }

    /**
     * Associates connection handle with new managed connection.
     *
     * @param newMc  new managed connection
     */

    public void associateConnection(ManagedConnectionImpl newMc)
        throws ResourceException 
    {
        checkIfValid();
        // dissociate handle from current managed connection
        mc.removeJavaMailConnection(this);
        // associate handle with new managed connection
        newMc.addJavaMailConnection(this);
        mc = newMc;
    }

    /**
     * Checks the validity of the physical connection to the EIS.
     */

    void checkIfValid() 
	throws ResourceException 
    {
        logger.finest(" -- In JavaMailConnectionImpl::checkIfValid mc=" + mc);
        if (mc == null) 
	{
            throw new ResourceException(resource.getString("INVALID_CONNECTION"));
        }
    }

    /**
     * Sets the physical connection to the EIS as invalid.
     * The physical connection to the EIS cannot be used any more.
     */

    void invalidate() 
    {
	logger.fine(" -- In JavaMailConnectionImpl::invalidate mc=" + mc);
        mc = null;
    }

    /**
     * Application-specific method. Fetches new messages from the mail server.
     *
     * @return an array of messages
     */

    public javax.mail.Message[] getNewMessages()
      throws ResourceException
    {
        checkIfValid();
        try
	{
	    return mc.getNewMessages(folder);
	} catch (Exception e) {
      	    logger.warning("ManagedConnectionImpl::getNewMessages threw exception: " + e);
	    throw new ResourceException(e.getMessage());
        } 
    }

    /**
     * Application-specific method. Fetches new message headers from the 
     * mail server.
     *
     * @return a String array of message headers
     */
    public String[] getNewMessageHeaders()
      throws ResourceException
    {
        checkIfValid();
        try
	{
	    return mc.getNewMessageHeaders(folder);
	} catch (Exception e) {
      	    logger.warning("ManagedConnectionImpl::getNewMessageHeaders threw exception: " + e);
	    throw new ResourceException(e.getMessage());
        } 
    } 
}
