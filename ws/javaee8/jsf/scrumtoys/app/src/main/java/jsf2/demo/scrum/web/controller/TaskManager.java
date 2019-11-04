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

import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.Task;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Named("taskManager")
@SessionScoped
public class TaskManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private Task currentTask;
    
    @Inject
    private StoryManager storyManager;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void construct() {
        init();
    }

    public void init() {
        Task task = new Task();
        Story currentStory = storyManager.getCurrentStory();
        task.setStory(currentStory);
        setCurrentTask(task);
    }

    public String create() {
        Task task = new Task();
        task.setStory(storyManager.getCurrentStory());
        setCurrentTask(task);
        return "create";
    }

    @Transactional
    public String save() {
        if (currentTask != null) {
            Task merged = currentTask;
            
            if (currentTask.isNew()) {
                em.persist(currentTask);
            } else if (!em.contains(currentTask)) {
                merged = em.merge(currentTask);
            }
            
            if (!currentTask.equals(merged)) {
                setCurrentTask(merged);
            }
            storyManager.getCurrentStory().addTask(merged);
        }
        return "show";
    }

    public String edit(Task task) {
        setCurrentTask(task);
        return "edit";
    }

    @Transactional
    public String remove(final Task task) {
        if (task != null) {
            if (em.contains(task)) {
                em.remove(task);
            } else {
                em.remove(em.merge(task));
            }
            storyManager.getCurrentStory().removeTask(task);
        }
        return "show";
    }

    @Transactional(dontRollbackOn=ValidatorException.class)
    public void checkUniqueTaskName(FacesContext context, UIComponent component, Object newValue) {
        final String newName = (String) newValue;
        Long count = null;
        
        Query query = em.createNamedQuery((currentTask.isNew()) ? "task.new.countByNameAndStory" : "task.countByNameAndStory");
        query.setParameter("name", newName);
        query.setParameter("story", storyManager.getCurrentStory());
        if (!currentTask.isNew()) {
            query.setParameter("currentTask", (!currentTask.isNew()) ? currentTask : null);
        }
        count = (Long) query.getSingleResult();
        if (count != null && count.longValue() > 0) {
            throw new ValidatorException(getFacesMessageForKey("task.form.label.name.unique"));
        }
    }

    public String cancelEdit() {
        return "show";
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public Story getStory() {
        return storyManager.getCurrentStory();
    }

    public void setStory(Story story) {
        storyManager.setCurrentStory(story);
    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public void setStoryManager(StoryManagerImpl storyManager) {
        this.storyManager = storyManager;
    }

    public String showStories() {
        return "/story/show";
    }


    @PreDestroy
    public void destroy() {
	currentTask = null;
	storyManager = null;
    }

}
