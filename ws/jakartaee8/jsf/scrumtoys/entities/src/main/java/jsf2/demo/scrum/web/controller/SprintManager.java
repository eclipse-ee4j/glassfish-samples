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
package jsf2.demo.scrum.web.controller;
import jsf2.demo.scrum.model.entities.Project;
import jsf2.demo.scrum.model.entities.Sprint;
public interface SprintManager {
    
    /*
     * This method is called by the JSR-303 SprintNameUniquenessConstraintValidator.
     * If it returns non-null, the result must be interpreted as the localized
     * validation message.
     *
     */
    public String checkUniqueSprintNameApplicationValidatorMethod(String newValue);
    
    public Sprint getCurrentSprint();
    public void setCurrentSprint(Sprint currentSprint);
    
    public Project getProject();
    
    public String edit(Sprint sprint);
    
    public String showStories(Sprint sprint);
    
    public String remove(final Sprint sprint);
    
    public String showDashboard(Sprint sprint);
    
    
}
