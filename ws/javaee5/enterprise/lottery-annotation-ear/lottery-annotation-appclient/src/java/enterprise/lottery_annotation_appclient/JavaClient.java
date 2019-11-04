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
package enterprise.lottery_annotation_appclient;

import javax.naming.InitialContext;

import enterprise.lottery_annotation_ejb_stateful.Lottery;
import enterprise.lottery_annotation_ejb_stateless.Dice;

public class JavaClient {

    public static void main(String args[]) {

        try {

            InitialContext ic = new InitialContext();

            Lottery lottery = 
                (Lottery) ic.lookup("enterprise.lottery_annotation_ejb_stateful.Lottery");

	    Dice dice;
	    for(int i=0; i<5; i++) {
            	dice = 
		    (Dice) ic.lookup("enterprise.lottery_annotation_ejb_stateless.Dice");
		lottery.select(dice.play());
            }

            String lotteryName = lottery.getName();
            String lotteryNumber = lottery.getNumber();
            String lotteryDate = lottery.getDate();
           
            String results = "Your" + " " + lotteryName + " " + 
                "quick pick, played on" + " " + lotteryDate +
                    " " + "is" + " " + lotteryNumber;       

            System.out.println(results);

        } catch(Exception e) {
	    System.out.println("Exception: " + e);
            e.printStackTrace();
        }

    }

}
