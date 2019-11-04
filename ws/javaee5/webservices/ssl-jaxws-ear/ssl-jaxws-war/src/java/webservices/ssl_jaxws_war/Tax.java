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

package webservices.ssl_jaxws_war;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * Tax WebService endpoint using the Java EE 5 annotations. 
 * Used to demonstrate https over ssl on Java EE 5 platform. 
 */

@WebService(
        name="Tax",
        serviceName="TaxService",
        targetNamespace="http://webservices/ssl_jaxws"
)
public class Tax {

    private static final double FED_TAX_RATE = 0.2; //20%

    public Tax() {
    }

    /*
     * Get the federal tax for given income and deductions
     */
    @WebMethod(operationName="getFedTax", action="urn:GetFedTax")
    public double getFedTax(double income, double deductions) {
        return ((income -  deductions) * FED_TAX_RATE);
    }

    
}
