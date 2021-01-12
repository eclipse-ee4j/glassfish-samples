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

package samples.connectors.mailconnector.share;

import jakarta.resource.cci.*;
import java.beans.*;

/**
 * This implementation class is used by an application component to pass
 * connection-specific info/properties to the getConnection method in the
 * JavaMailConnectionFactoryImpl class. This class is implemented as a JavaBeans
 * component.
 */

public class ConnectionSpecImpl implements ConnectionSpec {
    private PropertyChangeSupport changes    = new PropertyChangeSupport(this);
    private String                userName   = null;
    private String                password   = null;
    private String                folderName = null;
    private String                serverName = null;
    private String                protocol   = null;

    /**
     * ConnectionSpecImpl constructor (no arguments).
     */

    public ConnectionSpecImpl() {
    }

    /**
     * Returns the user name value.
     * 
     * @return String containing the user name
     */

    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns the password value.
     * 
     * @return String containing the password
     */

    public String getPassword() {
        return this.password;
    }

    /**
     * Returns the server name value.
     * 
     * @return String containing the server name
     */

    public String getServerName() {
        return this.serverName;
    }

    /**
     * Returns the folder name value.
     * 
     * @return String containing the folder name
     */

    public String getFolderName() {
        return this.folderName;
    }

    /**
     * Returns the protocol value.
     * 
     * @return String containing the protocol
     */

    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Sets the user name value.
     * 
     * @param userName
     *            the user name
     */

    public void setUserName(String userName) {
        String oldName = this.userName;
        this.userName = userName;
        changes.firePropertyChange("userName", oldName, userName);
    }

    /**
     * Sets the password value.
     * 
     * @param password
     *            the user password
     */

    public void setPassword(String password) {
        String oldPass = this.password;
        this.password = password;
        changes.firePropertyChange("password", oldPass, password);
    }

    /**
     * Sets the folder name value.
     * 
     * @param folderName
     *            the folder name
     */

    public void setFolderName(String folderName) {
        String oldFolderName = this.folderName;
        this.folderName = folderName;
        changes.firePropertyChange("folderName", oldFolderName, folderName);
    }

    /**
     * Sets the server name value.
     * 
     * @param serverName
     *            the server name
     */

    public void setServerName(String serverName) {
        String oldServerName = this.serverName;
        this.serverName = serverName;
        changes.firePropertyChange("serverName", oldServerName, serverName);
    }

    /**
     * Sets the protocol value.
     * 
     * @param protocol
     *            the server name
     */

    public void setProtocol(String protocol) {
        String oldProtocol = this.protocol;
        this.protocol = protocol;
        changes.firePropertyChange("protocol", oldProtocol, protocol);
    }

    /**
     * Associate PropertyChangeListener with the ConnectionSpecImpl in order to
     * notify about properties changes.
     * 
     * @param listener
     *            the listener to be associated with the connection spec
     */

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    /**
     * Delete association of PropertyChangeListener with the ConnectionSpecImpl.
     * 
     * @param listener
     *            the listener to be deleted
     */

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }
}
