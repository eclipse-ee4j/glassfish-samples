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
package enterprise.lottery_annotation_ejb_stateful;


import javax.ejb.EJB;
import javax.ejb.Stateful;

import enterprise.lottery_annotation_ejb_stateless.Date;


@Stateful
public class LotteryBean implements Lottery {

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date.today();
    }

    public void select(int number) {
        if( (number > -1) && (number < 10) ) {
            this.number = this.number + SPACE + 
                java.lang.Integer.toString(number);
	}
    }

    public void setName(String name) {
 	this.name = name;
    }


    //Dependency injection to get an instance of the Date EJB.
    @EJB(name="Date") 
    private Date date;

    private String name = "Super Lotto";
    private String number = "";
    private static final String SPACE = " "; 
}
