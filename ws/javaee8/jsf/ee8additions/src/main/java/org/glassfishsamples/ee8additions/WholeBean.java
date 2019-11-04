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
package org.glassfishsamples.ee8additions;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Named("wholeBean")
@RequestScoped
@Whole
public class WholeBean implements Serializable {
    
    @NotNull
    private String value1;
    
    @Min(2)
    private String value2;

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }
    
    public String submit() {
        return "";
    }
}
