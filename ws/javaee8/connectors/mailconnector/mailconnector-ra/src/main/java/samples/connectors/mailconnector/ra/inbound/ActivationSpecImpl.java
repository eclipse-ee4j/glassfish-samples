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

package samples.connectors.mailconnector.ra.inbound;

import javax.resource.ResourceException;
import javax.resource.spi.*;

/**
 * This class implements the Activation Spec class
 * of the Sample mailconnector connector.
 * @author Alejandro Murillo
 */

@Activation(
        messageListeners = {samples.connectors.mailconnector.api.JavaMailMessageListener.class}
)
public class ActivationSpecImpl implements javax.resource.spi.ActivationSpec, 
                                           java.io.Serializable
{
    private ResourceAdapter resourceAdapter = null;
    /**
     * TODO : Use bean validation mechanism to specify "required-config-property" equivalent of
     * deployment descriptor (ra.xml) in the bean via @NotNull
     */
    @ConfigProperty()
    // serverName property value
    private String serverName = "";

    @ConfigProperty()
    // userName property value
    private String userName = "";

    @ConfigProperty()
    // password property value
    private String password = "";

    @ConfigProperty()
    // folderName property value
    private String folderName = "Inbox";
    
    // protocol property value
    // Normally imap or pop3
    @ConfigProperty(
            description = "Normally imap or pop3"
    )
    private String protocol = "imap";
    
    // Polling interval (seconds)
    private String interval = "30";
    
    /**
     * Constructor. Creates a new instance of the base activation spec.
     */

    public ActivationSpecImpl() { }

    /**
     * Returns the value of the serverName property.
     *
     * @return    String containing the value of the serverName 
     *            property
     */

    public String getServerName() 
    {
        return this.serverName;
    }

    /**
     * Sets the value of the serverName property.
     *
     * @param serverName  String containing the value to be assigned 
     *                    to serverName
     */

    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    /**
     * Returns the value of the userName property.
     *
     * @return   String containing the value of the userName property
     */

    public String getUserName() 
    {
        return this.userName;
    }

    /**
     * Sets the value of the userName property.
     *
     * @param userName    String containing the value to be assigned 
     *                    to userName
     */

    public void setUserName(String userName)
    {
            this.userName = userName;
    }

    /**
     * Returns the value of the password property.
     *
     * @return    String containing the value of the password property
     */

    public String getPassword() 
    {
        return this.password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param password    String containing the value to be 
     *                    assigned to password
     */

    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Returns the value of the folderName property.
     *
     * @return  String containing the value of the folderName property
     */

    public String getFolderName() 
    {
        return this.folderName;
    }

    /**
     * Sets the value of the folderName property.
     *
     * @param folderName    String containing the value to be assigned
     *                      to folderName
     */

    public void setFolderName(String folderName)
    {
        this.folderName = folderName;
    }

    /**
     * Returns the value of the protocol property.
     *
     * @return    String containing the value of the protocol property
     */

    public String getProtocol() 
    {
        return this.protocol;
    }

    /**
     * Sets the value of the protocol property.
     *
     * @param protocol    String containing the value to be assigned 
     *                    to protocol
     */

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    /**
     * Returns the value of the interval property.
     *
     * @return    String containing the value of the interval property
     */

    public String getInterval() 
    {
        return this.interval;
    }

    /**
     * Sets the value of the interval property.
     *
     * @param interval    String containing the value to be assigned 
     *                    to interval
     */

    public void setInterval(String interval) 
    {
        this.interval = interval;
    }

    /**
     * Validates the configuration properties.
     * TBD: verify that a connection to the mail server can be done
     *
     * @exception    InvalidPropertyException
     */

    public void validate() 
	throws InvalidPropertyException 
    { }

    /**
     * Sets the resource adapter.
     *
     * @param ra  the resource adapter
     */

    public void setResourceAdapter(ResourceAdapter ra)
        throws ResourceException
    {
        this.resourceAdapter = ra;
    }

    /**
     * Gets the resource adapter.
     *
     * @return   the resource adapter
     */
    public ResourceAdapter getResourceAdapter()
    {
        return resourceAdapter;
    }

}
