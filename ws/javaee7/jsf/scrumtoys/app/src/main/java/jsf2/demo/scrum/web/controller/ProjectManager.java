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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Map;
import javax.faces.context.ExternalContext;

import javax.transaction.Transactional;

import javax.persistence.PersistenceContext;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Named("projectManager")
@SessionScoped
public class ProjectManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private Project currentProject;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void construct() {
        Project project = new Project();
        setCurrentProject(project);
    }

    @PreDestroy
    public void destroy() {
        currentProject = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("projectManager");
                }
            }
        }
    }

    public String create() {
        Project project = new Project();
        setCurrentProject(project);
        return "create";
    }

    @Transactional
    public String save() {
        Project cur = getCurrentProject();
        if (cur != null) {
            Project merged = em.merge(cur);
            if (!currentProject.equals(merged)) {
                setCurrentProject(merged);
            }
        }
        return "show";
    }

    public String edit(Project project) {
        setCurrentProject(project);
        // Using implicity navigation, this request come from /projects/show.xhtml and directs to /project/edit.xhtml
        return "edit";
    }

    @Transactional
    public String remove(final Project project) {
        if (project != null) {
            Object toRemove = em.find(Project.class, project.getId());
            assert(null != toRemove);
            
            em.remove(toRemove);
        }
        // Using implicity navigation, this request come from /projects/show.xhtml and directs to /project/show.xhtml
        // could be null instead
        return "show";
    }

    @Transactional(dontRollbackOn=ValidatorException.class)
    public void checkUniqueProjectName(FacesContext context, UIComponent component, Object newValue) {
        final String newName = (String) newValue;
        Long count;
        Query query = em.createNamedQuery((getCurrentProject().isNew()) ? "project.new.countByName" : "project.countByName");
        query.setParameter("name", newName);
        if (!currentProject.isNew()) {
            query.setParameter("currentProject", getCurrentProject());
        }
        count = (Long) query.getSingleResult();
        if (count != null && count > 0) {
            throw new ValidatorException(getFacesMessageForKey("project.form.label.name.unique"));
        }
    }

    public String cancelEdit() {
        // Implicity navigation, this request come from /projects/edit.xhtml and directs to /project/show.xhtml
        return "show";
    }

    public String showSprints(Project project) {
        setCurrentProject(project);
        // Implicity navigation, this request come from /projects/show.xhtml and directs to /project/showSprints.xhtml
        return "showSprints";
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public String viewSprints() {
        return "/sprint/show";
    }


}
