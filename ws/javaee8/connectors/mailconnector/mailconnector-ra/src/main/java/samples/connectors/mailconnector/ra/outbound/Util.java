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

import java.util.*;
import java.util.logging.*;

import javax.resource.ResourceException;
import javax.resource.spi.*;
import javax.resource.spi.security.*;
import javax.security.auth.Subject;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Utilities. The following methods handle authentication.
 * Note: If subject is null, credential is created from the
 * ConnectionRequestInfo user/password info. Otherwise it is created using the
 * subject object.
 * @author Alejandro E. Murillo
 */

public class Util {
    static Logger  logger  = Logger.getLogger(
                             "samples.connectors.mailconnector.ra.outbound",
                             "samples.connectors.mailconnector.ra.outbound.LocalStrings");
    static ResourceBundle resource = java.util.ResourceBundle
                                    .getBundle("samples.connectors.mailconnector.ra.outbound.LocalStrings");

    /**
     * Returns a PasswordCredential.
     * 
     * @param mcf
     *            a ManagedConnectionFactory object
     * @param subject
     *            security context as JAAS subject
     * @param info
     *            ConnectionRequestInfo instance
     * 
     * @return a PasswordCredential
     */

    static public PasswordCredential getPasswordCredential(
            final ManagedConnectionFactory mcf, final Subject subject,
            ConnectionRequestInfo info) throws ResourceException {
        if (subject == null) {
            if (info == null) {
                logger.fine("\tUtil::GetPasswordCred: INFO is null");
                return null;
            } else {

                ConnectionRequestInfoImpl myinfo = (ConnectionRequestInfoImpl) info;

                // Can't create a PC with null values

                if (myinfo.getUserName() == null
                        || myinfo.getPassword() == null) {
                    logger.fine("\tUtil::GetPasswordCred: User or password is null");
                    return null;
                }

                char[] password = myinfo.getPassword().toCharArray();

                PasswordCredential pc = new PasswordCredential(
                        myinfo.getUserName(), password);

                pc.setManagedConnectionFactory(mcf);
                logger.fine("\tUtil::GetPasswordCred: returning a created PC");
                return pc;
            }
        } else {
            PasswordCredential pc = (PasswordCredential) AccessController
                    .doPrivileged(new PrivilegedAction() {
                        public Object run() {
                            Set creds = subject
                                    .getPrivateCredentials(PasswordCredential.class);
                            Iterator iter = creds.iterator();
                            while (iter.hasNext()) {
                                PasswordCredential temp = (PasswordCredential) iter
                                        .next();
                                if (temp != null
                                        && temp.getManagedConnectionFactory() != null
                                        && temp.getManagedConnectionFactory()
                                                .equals(mcf)) {
                                    return temp;
                                }
                            }
                            return null;
                        }
                    });
            if (pc == null) {
                throw new java.lang.SecurityException(
                        resource.getString("util.no_credential"));
            } else {
                logger.fine("\tUtil::GetPasswordCred: returning a FOUND PC");
                return pc;
            }
        }
    }

    /**
     * Determines whether two strings are the same.
     * 
     * @param a
     *            first string
     * @param b
     *            second string
     * 
     * @return true if the two strings are equal; false otherwise
     */

    static public boolean isEqual(String a, String b) {
        if (a == null) {
            return (b == null);
        } else {
            return a.equals(b);
        }
    }

    /**
     * Determines whether two PasswordCredentials are the same.
     * 
     * @param a
     *            first PasswordCredential
     * @param b
     *            second PasswordCredential
     * 
     * @return true if the two parameters are equal; false otherwise
     */

    static public boolean isPasswordCredentialEqual(PasswordCredential a,
            PasswordCredential b) {
        if (a == b)
            return true;
        if ((a == null) && (b != null))
            return false;
        if ((a != null) && (b == null))
            return false;
        if (!isEqual(a.getUserName(), b.getUserName()))
            return false;

        String p1 = null;
        String p2 = null;

        if (a.getPassword() != null) {
            p1 = new String(a.getPassword());
        }
        if (b.getPassword() != null) {
            p2 = new String(b.getPassword());
        }
        return (isEqual(p1, p2));
    }
}
