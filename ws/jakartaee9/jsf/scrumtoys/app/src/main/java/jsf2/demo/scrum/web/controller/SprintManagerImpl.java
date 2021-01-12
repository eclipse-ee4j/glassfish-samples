/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package jsf2.demo.scrum.web.controller;

import jsf2.demo.scrum.model.entities.Project;
import jsf2.demo.scrum.model.entities.Sprint;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.io.Serializable;
import java.util.Map;
import jakarta.faces.context.ExternalContext;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Named("sprintManager")
@SessionScoped
public class SprintManagerImpl extends AbstractManager implements Serializable, SprintManager {

    private static final long serialVersionUID = 1L;
    private Sprint currentSprint;
    @Inject
    private ProjectManager projectManager;
    private Project currentProject;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void construct() {
        init();
    }

    @PreDestroy
    public void destroy() {
        projectManager = null;
        currentProject = null;
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("sprintManager");
                }
            }
        }

    }

    public void init() {
        Sprint sprint = new Sprint();
        Project pmCurrentProject = getProjectManager().getCurrentProject();
        sprint.setProject(pmCurrentProject);
        setCurrentSprint(sprint);
    }

    public String create() {
        Sprint sprint = new Sprint();
        sprint.setProject(getProjectManager().getCurrentProject());
        setCurrentSprint(sprint);
        return "create";
    }

    @Transactional
    public String save() {
        if (currentSprint != null) {
            Sprint merged = currentSprint;
            if (currentSprint.isNew()) {
                em.persist(currentSprint);
            } else if (!em.contains(currentSprint)) {
                merged = em.merge(currentSprint);
            }
            if (!currentSprint.equals(merged)) {
                setCurrentSprint(merged);
            }
            getProjectManager().getCurrentProject().addSprint(merged);
        }
        return "show";
    }
    
    
    public String edit(Sprint sprint) {
        setCurrentSprint(sprint);
        return "edit";
    }

    @Transactional
    public String remove(final Sprint sprint) {
        if (sprint != null) {
            em.remove(em.find(Sprint.class, sprint.getId()));
        }
        return "show";
    }

    /*
    * This method can be pointed to by a validator methodExpression, such as:
    *
    * <h:inputText id="itName" value="#{sprintManager.currentSprint.name}" required="true"
    *   requiredMessage="#{i18n['sprint.form.label.name.required']}" maxLength="30" size="30"
    *   validator="#{sprintManager.checkUniqueSprintName}" />
    */

    public void checkUniqueSprintNameFacesValidatorMethod(FacesContext context, UIComponent component, Object newValue) {

        final String newName = (String) newValue;
        String message = checkUniqueSprintNameApplicationValidatorMethod(newName);
        if (null != message) {
            throw new ValidatorException(getFacesMessageForKey("sprint.form.label.name.unique"));
        }
    }


    /*
    * This method is called by the JSR-303 SprintNameUniquenessConstraintValidator.
    * If it returns non-null, the result must be interpreted as the localized
    * validation message.
    *
    */

    @Transactional
    public String checkUniqueSprintNameApplicationValidatorMethod(String newValue) {
        String message = null;

        final String newName = newValue;
        Long count;
        Query query = em.createNamedQuery((currentSprint.isNew()) ? "sprint.new.countByNameAndProject" : "sprint.countByNameAndProject");
        query.setParameter("name", newName);
        query.setParameter("project", getProjectManager().getCurrentProject());
        if (!currentSprint.isNew()) {
            query.setParameter("currentSprint", currentSprint);
        }
        count = (Long) query.getSingleResult();
        if (count != null && count > 0) {
            message = getFacesMessageForKey("sprint.form.label.name.unique").getSummary();
        }
        return message;
    }

    public String cancelEdit() {
        return "show";
    }

    public String showStories(Sprint sprint) {
        setCurrentSprint(sprint);
        return "showStories";
    }

    public String showDashboard(Sprint sprint) {
        setCurrentSprint(sprint);
        return "showDashboard";
    }

    public Sprint getCurrentSprint() {
        return currentSprint;
    }

    public void setCurrentSprint(Sprint currentSprint) {
        this.currentSprint = currentSprint;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public Project getProject() {
        Project pmCurrentProject = projectManager.getCurrentProject();
        // Verify if the currentProject is out of date
        // If there is a new CurrentProject we need to update sprintList and set currentSprint to null and tell user he/she needs to select a Sprint
        if (pmCurrentProject != currentProject) {
            this.setCurrentSprint(null);
            this.currentProject = pmCurrentProject;
        }
        return currentProject;
    }

    public void setProject(Project project) {
        projectManager.setCurrentProject(project);
    }

}
