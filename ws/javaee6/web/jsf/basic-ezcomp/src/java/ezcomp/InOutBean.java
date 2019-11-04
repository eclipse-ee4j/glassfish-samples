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

package ezcomp;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 * Managed Bean for the inout composite component demo.
 */

@ManagedBean(name = "inout")
@SessionScoped
public class InOutBean implements Serializable {

    private static final long serialVersionUID = -5980327949241062555L;

    private static final Logger LOGGER =
          Logger.getLogger(InOutBean.class.getName());

    private String value = "Hello World!";


    // ---------------------------------------------------------- Public Methods


    public void setValue(String value) {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                       "[InOutBean::setValue]: {0}",
                       value);
        }

        this.value = value;

    }

    
    public String getValue() {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                       "[InOutBean::getValue]: {0}",
                       value);
        }

        return value;

    }


}
