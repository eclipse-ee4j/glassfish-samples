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


/**
 * JavaMailMessageListener interface implemented by JavaMailMessageBean.
 * 
 * @author Alejandro E. Murillo
 */

public interface JavaMailMessageListener
{
    /**
     * Message-driven bean method invoked by the EJB container.
     *
     * @param message  the incoming message
     */

    public void onMessage(javax.mail.Message message);
}
