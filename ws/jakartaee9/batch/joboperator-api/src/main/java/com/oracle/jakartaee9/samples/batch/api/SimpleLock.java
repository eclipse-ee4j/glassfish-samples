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
package com.oracle.jakartaee9.samples.batch.api;

import jakarta.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: makannan
 * Date: 4/1/13
 * Time: 12:11 AM
 * To change this template use File | Settings | File Templates.
 */
@SessionScoped
public class SimpleLock
    implements Serializable {

    @Override
    public String toString() {
        return "SimpleLock{" +
                System.identityHashCode(this) +
                '}';
    }

}
