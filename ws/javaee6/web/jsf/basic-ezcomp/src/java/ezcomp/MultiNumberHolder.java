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

package ezcomp;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name="multinumber")
@SessionScoped
public class MultiNumberHolder implements Serializable {

    private static final long serialVersionUID = 6733229187968502934L;

    private Integer i1 = 1;
    private Integer i2 = 2;

    public void setNumber1(Integer i) {
        this.i1 = i;
    }

    public Integer getNumber1() {
        return i1;
    }

    public void setNumber2(Integer i) {
        this.i2 = i;
    }

    public Integer getNumber2() {
        return i2;
    }

}
