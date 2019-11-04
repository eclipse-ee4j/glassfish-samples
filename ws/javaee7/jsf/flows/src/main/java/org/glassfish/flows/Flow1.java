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
package org.glassfish.flows;

import javax.enterprise.inject.Produces;
import javax.faces.flow.Flow;
import javax.faces.flow.builder.FlowBuilder;
import javax.faces.flow.builder.FlowBuilderParameter;
import javax.faces.flow.builder.FlowDefinition;

/**
 */
public class Flow1 {

    private static final long serialVersionUID = -7623501087369765218L;

    @Produces @FlowDefinition
    public Flow defineFlow(@FlowBuilderParameter FlowBuilder flowBuilder) {
        String flowId = "flow1";
        flowBuilder.id("", flowId);
        flowBuilder.viewNode(flowId, "/" + flowId + "/" + flowId + ".xhtml").markAsStartNode();
        
        flowBuilder.returnNode("taskFlowReturn1").
                fromOutcome("#{flow1Bean.returnValue}");
        
        flowBuilder.inboundParameter("param1FromFlow2", "#{flowScope.param1Value}");
        flowBuilder.inboundParameter("param2FromFlow2", "#{flowScope.param2Value}");
        
        flowBuilder.flowCallNode("call2").flowReference("", "flow2").
                outboundParameter("param1FromFlow1", "param1 flow1 value").
                outboundParameter("param2FromFlow1", "param2 flow1 value");

        return flowBuilder.getFlow();
    }
}
