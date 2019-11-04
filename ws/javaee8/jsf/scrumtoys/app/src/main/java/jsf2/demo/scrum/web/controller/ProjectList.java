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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import jsf2.demo.scrum.model.entities.Project;

/**
 * @author Eder Magalhaes
 */
@Named("projectList")
@ViewScoped
public class ProjectList extends AbstractManager implements Serializable{

    @Inject
    private ProjectManager projectManager;

    private DataModel<Project> projects;
    private List<SelectItem> projectItems;
    private List<Project> projectList;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void init() {
        Query query = em.createNamedQuery("project.getAll");
        setProjectList((List<Project>) query.getResultList());
        projectItems = new LinkedList<SelectItem>();
        projectItems.add(new SelectItem(new Project(), "-- Select one project --"));
        if (getProjectList() != null) {
            projects = new ListDataModel<Project>(getProjectList());
            for (Project p : getProjectList()) {
                projectItems.add(new SelectItem(p, p.getName()));
            }
        }
    }
    
    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public DataModel<Project> getProjects() {
        return projects;
    }

    public void setProjects(DataModel<Project> projects) {
        this.projects = projects;
    }

    public List<SelectItem> getProjectItems() {
        return projectItems;
    }

    public void setProjectItems(List<SelectItem> projectItems) {
        this.projectItems = projectItems;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public String edit() {
        return projectManager.edit(projects.getRowData());
    }

    public String remove() {
        String result = projectManager.remove(projects.getRowData());
        init();
        return result;
    }

    public String showSprints() {
        return projectManager.showSprints(projects.getRowData());
    }
}
