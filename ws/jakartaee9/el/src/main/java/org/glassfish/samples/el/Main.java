/*
 	Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
	
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

package org.glassfish.samples.el;

import jakarta.el.*;

/**
 * A Sample for Expression Language 3.0
 * @author Kin-man Chung
 */
public class Main {

    ELProcessor elp = new ELProcessor();

    public static void main(String[] args) {
        Main test = new Main();
        test.run();
    }

    void run() {
        // obligatory test
        eval("'Hello, world!'");
        // static field reference
        eval("Boolean.TRUE");
        // static method reference
        eval("Integer.numberOfTrailingZeros(16)");
        // string concatenation
        eval("10 += 11");
        // assignment and semicolon operator
        eval("xx = 100; yy = 11; xx+yy");
        // lambda expression: immediate invokation
        eval("(x->x+1)(10)");
        // lambda expression: set to variable and recursive invokation
        eval("fact = n->n==0? 1: n*fact(n-1); fact(5)");
        // List construction
        eval("['eenie', 'meenie', 'miney', 'moe']");
        // Map construction
        eval("{'one':10, 'two':20, 'three':300}");
        // Simple collection operation
        eval("[1,2,3,4,5,6,7,8].stream().filter(i->i%2 == 0) \n" +
             "                          .map(i->i*10) \n" +
             "                          .toList()");
        // sort a list in descending order
        eval("[1,5,3,7,4,2,8].stream().sorted((i,j)->j-i).toList()");
    }

    void eval(String input) {
        System.out.println("\n====");
        System.out.println("Input EL string: " + input);
        Object ret = elp.eval(input);
        System.out.println("Output Value: " + ret);
    }
}
