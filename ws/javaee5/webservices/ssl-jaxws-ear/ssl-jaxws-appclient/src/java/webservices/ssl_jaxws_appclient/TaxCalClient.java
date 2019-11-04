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

package webservices.ssl_jaxws_appclient;

import webservices.ssl_jaxws.Tax;
import webservices.ssl_jaxws.TaxService;
import javax.xml.ws.WebServiceRef;
import javax.xml.rpc.Stub;

/**
 * This is Application Client program Tax Webservice using SSL port.
 */
        
public class TaxCalClient {
    /*
     * Tests the getStateTax and getFedTax with expected values. If value 
     * matched,then test PASSED else FAILED.
     */
        @WebServiceRef(wsdlLocation="http://localhost:8080/ssl-jaxws-war/TaxService?wsdl")
        static TaxService service;

    public static void main (String[] args) {

        if(args.length<2){
            System.out.println("USage: TaxCalClient <income> <deductions>");
            return;
        } 
        double income=Double.parseDouble(args[0]);
        double deductions=Double.parseDouble(args[1]);
        
        System.out.println("Invoking TaxService...");
        try { 
            TaxCalClient client = new TaxCalClient();
            double fedTax = client.getFedTax(income, deductions);
            System.out.println("Income="+income+"\nDeductions="+deductions+
                            "\nFederal tax=" +fedTax);
            
        } catch(Exception e) {
                e.printStackTrace();
        }
    }
    

    public double getFedTax(double income, double deductions) throws Exception {        
          double fedTax = 0.0;
            if (service!=null) {
                Tax port = service.getTaxPort();
                //((Stub)port)._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY,
                //        taxEndpoint);

                fedTax = port.getFedTax(income, deductions);                
            }else {
                throw new Exception("Error: Not able to get the tax service port " +
                        "and is null!");
            }
           return fedTax;
    }
}

