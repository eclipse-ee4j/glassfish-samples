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

import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;

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


@Named("storyManager")
@SessionScoped
public class StoryManagerImpl extends AbstractManager implements Serializable, StoryManager {

    private static final long serialVersionUID = 1L;
    @Inject
    private SprintManager sprintManager;
    private Story currentStory;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void construct() {
        init();
    }

    @PreDestroy
    public void destroy() {
        sprintManager = null;
        currentStory = null;
        
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != context) {
            ExternalContext extContext = context.getExternalContext();
            if (null != extContext) {
                Map sessionMap = extContext.getSessionMap();
                if (null != sessionMap) {
                    sessionMap.remove("storyManager");
                }
            }
        }

    }

    public void init() {
        Sprint currentSprint = sprintManager.getCurrentSprint();

        if (currentSprint != null) {
            Story story = new Story();
            story.setSprint(currentSprint);
            setCurrentStory(story);
        }
    }

    public String create() {
        Story story = new Story();
        story.setSprint(sprintManager.getCurrentSprint());
        setCurrentStory(story);
        return "create";
    }

    @Transactional
    public String save() {
        if (currentStory != null) {
            Story merged = em.merge(currentStory);
            setCurrentStory(merged);
            sprintManager.getCurrentSprint().addStory(merged);
        }
        return "show";
    }

    public String edit(Story story) {
        setCurrentStory(story);
        return "edit";
    }

    @Transactional
    public String remove(final Story story) {
        if (story != null) {

            em.remove(em.merge(story));

            sprintManager.getCurrentSprint().removeStory(story);
        }
        return "show";
    }
    
    @Transactional(dontRollbackOn=ValidatorException.class)
    public void checkUniqueStoryName(FacesContext context, UIComponent component, Object newValue) {
        final String newName = (String) newValue;
        Long count = null;

        Query query = em.createNamedQuery((currentStory.isNew()) ? "story.new.countByNameAndSprint" : "story.countByNameAndSprint");
        query.setParameter("name", newName);
        query.setParameter("sprint", sprintManager.getCurrentSprint());
        if (!currentStory.isNew()) {
            query.setParameter("currentStory", currentStory);
        }
        count = (Long) query.getSingleResult();
        if (count != null && count > 0) {
            throw new ValidatorException(getFacesMessageForKey("story.form.label.name.unique"));
        }
    }

    public String cancelEdit() {
        return "show";
    }
    
    public String showTasks(Story story) {
        setCurrentStory(story);
        return "showTasks";
    }

    public Story getCurrentStory() {
        return currentStory;
    }

    public void setCurrentStory(Story currentStory) {
        this.currentStory = currentStory;
    }

    public Sprint getSprint() {
        return sprintManager.getCurrentSprint();
    }

    public void setSprint(Sprint sprint) {
        sprintManager.setCurrentSprint(sprint);
    }

    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }


}
