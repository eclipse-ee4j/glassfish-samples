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

package enterprise.lottery_annotation_ejb_stateful_test;

import org.junit.*;

import javax.naming.InitialContext;
import enterprise.lottery_annotation_ejb_stateful.Lottery;

public class LotteryTest {

    @Before public void Setup() {
	try {
            ic = new InitialContext();
            lottery =
		(Lottery) ic.lookup("enterprise.lottery_annotation_ejb_stateful.Lottery");
	} catch(Exception e) {
	    e.printStackTrace();
       	    Assert.fail("Encountered exception in LotteryTest:setUp. Bean lookup failed.");
        }
    }

    @Test public void getDateTest1() {
        Assert.assertNotNull("Date can not be Null.", lottery.getDate());
    }

    @Test public void getDateTest2() {
        if("".equals(lottery.getDate())) {
            Assert.fail("Date can not be Empty.");
        }
    }

    @Test public void getNameTest1() {
        Assert.assertNotNull("Name can not be Null.", lottery.getName());
    }

    @Test public void getNameTest2() {
        if("".equals(lottery.getName())) {
            Assert.fail("Name can not be Empty.");
        }
    }

    @Test public void getNameTest3() {

        lottery.setName(LOTTERY_NAME);
        Assert.assertEquals("Name returned is not the same as set by most recent setName() call.", 
            LOTTERY_NAME, lottery.getName()); 
    }

    @Test public void getNumberTest1() {
        Assert.assertNotNull("Number can not be Null.", lottery.getNumber());
    }

    @Test public void getNunmberTest2() {

        Assert.assertEquals("Number should be Empty(reset state) at start.",
            "", lottery.getNumber());

        lottery.select(NUMBER_IN_RANGE_1);
        Assert.assertEquals("Number is not updated correctly on initial select(int_in_range).",
            String.valueOf(NUMBER_IN_RANGE_1), lottery.getNumber().trim());

        lottery.select(NUMBER_OUT_OF_RANGE);
        Assert.assertEquals("Number is not updated correctly on select(int_out_of_range).",
            String.valueOf(NUMBER_IN_RANGE_1), lottery.getNumber().trim());

        lottery.select(NUMBER_IN_RANGE_2); 
        Assert.assertEquals("Number is not updated correctly on subsequent select(int_in_range).",
            String.valueOf(NUMBER_IN_RANGE_1) + String.valueOf(NUMBER_IN_RANGE_2),
                eleminateSpaces(lottery.getNumber()));
    }


    //Removes any spaces from the given string.
    private static String eleminateSpaces(String string){
        if(!(string == null || string.length() <= 0)){
            int index = string.indexOf(' ');
            while(index != -1){
                if(index == 0){
                    string = string.substring(1);
                } else {
                    if(index == (string.length() - 1)){
                        string = string.substring(0,string.length()-1);
                    } else {
                        string = string.substring(0,index) +
                            string.substring(index + 1);
                    }
                }
                index = string.indexOf(' ');
            }
        }
        return string;
    }

    static final String LOTTERY_NAME = "Super Play"; 
    static final int NUMBER_IN_RANGE_1 = 5;       //0..9
    static final int NUMBER_IN_RANGE_2 = 5;       //0..9
    static final int NUMBER_OUT_OF_RANGE = 15;    //!(0..9) 

    private InitialContext ic = null;
    private Lottery lottery = null;

}
