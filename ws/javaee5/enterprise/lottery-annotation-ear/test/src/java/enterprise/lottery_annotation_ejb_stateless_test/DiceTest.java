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

package enterprise.lottery_annotation_ejb_stateless_test;

import org.junit.*;

import javax.naming.InitialContext;
import enterprise.lottery_annotation_ejb_stateless.Dice;

public class DiceTest {

    @Test public void Test1() {
	try {
            InitialContext ic = new InitialContext();
            Dice dice =
		(Dice) ic.lookup("enterprise.lottery_annotation_ejb_stateless.Dice");
  	    int number = dice.play();
            if((number < 0) || (number > 9)) {
		Assert.fail("Invalid number generated.");
            }
	} catch(Exception e) {
	    e.printStackTrace();
       	    Assert.fail("Encountered exception in DiceTest:Test1");
	}
    }

}
