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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import jsf2.demo.scrum.model.entities.Project;
import jsf2.demo.scrum.model.entities.Sprint;

/**
 * @author Eder Magalhaes
 */
@Named("sprintList")
@ViewScoped
public class SprintList extends AbstractManager implements Serializable {
    
    @Inject
    private SprintManager sprintManager;
    
    private DataModel<Sprint> sprints;
    
    private List<Sprint> sprintList;
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    @Transactional
    public void init() {
        List<Sprint> temp = Collections.EMPTY_LIST;
        Project p = sprintManager.getProject();
        if (p != null) {
            Query query = em.createNamedQuery("sprint.getByProject");
            query.setParameter("project", p);
            temp = (List<Sprint>) query.getResultList();
        }
        setSprintList(temp);
    }
    
    public DataModel<Sprint> getSprints() {
        this.sprints = new ListDataModel<Sprint>(sprintList);
        return this.sprints;
    }

    public void setSprints(DataModel<Sprint> sprints) {
        this.sprints = sprints;
    }

    public void setSprintList(List<Sprint> sprintList) {
        this.sprintList = sprintList;
    }

    public List<Sprint> getSprintList() {
        return sprintList;
    }
    
    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }
    
    public String edit() {
        return sprintManager.edit(sprints.getRowData());
    }
    
    public String remove() {
        String result = sprintManager.remove(sprints.getRowData());
        init();
        return result;
    }
    
    public String showStories() {
        return sprintManager.showStories(sprints.getRowData());
    }

    public String showDashboard() {
        return sprintManager.showDashboard(sprints.getRowData());
    }
    
}
