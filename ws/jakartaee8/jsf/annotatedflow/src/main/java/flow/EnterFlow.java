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
package flow;

import java.io.Serializable;
import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;

public class EnterFlow implements Serializable {

    private static final long serialVersionUID = -7623501087369765218L;

    @FlowDefinition
    @Produces
    private Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
        FlowBuilder builder = flowBuilder.id("", "flow");
        builder.viewNode("enter", "/enterflow/stepA.xhtml").markAsStartNode();
        Flow flow = builder.getFlow();
        return flow;
    }
}
