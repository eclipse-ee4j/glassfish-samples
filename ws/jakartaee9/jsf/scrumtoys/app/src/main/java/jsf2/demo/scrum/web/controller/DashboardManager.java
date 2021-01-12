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
import jsf2.demo.scrum.model.entities.Task;

import jakarta.annotation.PreDestroy;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jsf2.demo.scrum.model.entities.TaskStatus;


@Named("dashboardManager")
@ViewScoped
public class DashboardManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TaskManager taskManager;
    @Inject
    private SprintManager sprintManager;
    @Inject
    private StoryManager storyManager;
    @Inject
    private StoryList storyList;
    
    @PersistenceContext
    private EntityManager em;

    private ListDataModel<Task> toDoTasks;
    private ListDataModel<Task> workingTasks;
    private ListDataModel<Task> doneTasks;

    @PostConstruct
    public void init() {
        List<Task> toDoTasksList = getTasksByStatus(TaskStatus.TODO);
        toDoTasks = new ListDataModel<Task>(toDoTasksList);
        
        List<Task> workingTasksList = getTasksByStatus(TaskStatus.WORKING);
        workingTasks = new ListDataModel<Task>(workingTasksList);
        
        List<Task> doneTasksList = getTasksByStatus(TaskStatus.DONE);
        doneTasks = new ListDataModel<Task>(doneTasksList);
    }
    
    @PreDestroy
    public void destroy() {
        toDoTasks = null;
        workingTasks = null;
        doneTasks = null;
    }

    public Sprint getSprint() {
        return getSprintManager().getCurrentSprint();
    }

    public void setSprint(Sprint sprint) {
        this.getSprintManager().setCurrentSprint(sprint);
    }

    public DataModel<Story> getStories() {
        return storyList.getStories();
    }

    public void setStories(DataModel<Story> stories) {
        storyList.setStories(stories);
    }
    
    @Transactional
    private List<Task> getTasksByStatus(final TaskStatus status) {
        List<Task> result = Collections.EMPTY_LIST;
        Sprint s = storyManager.getSprint();
        if (s != null) {
            Query query = em.createNamedQuery("task.getByStatusAndSprint");
            query.setParameter("status", status);
            query.setParameter("sprint", s);
            
            result = (List<Task>) query.getResultList();
        }
        return result;
    }

    public ListDataModel<Task> getToDoTasks() {
        /*List<Task> toDoTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(toDoTasksList);
        }
        for (Story story : storyList.getStoryList()) {
            toDoTasksList.addAll(story.getTodoTasks());
        }
        toDoTasks = new ListDataModel<Task>(toDoTasksList);*/
        return toDoTasks;
    }

    public ListDataModel<Task> getWorkingTasks() {
        /*List<Task> workingTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(workingTasksList);
        }
        for (Story story : storyList.getStoryList()) {
            workingTasksList.addAll(story.getWorkingTasks());
        }
        workingTasks = new ListDataModel<Task>(workingTasksList);*/
        return workingTasks;
    }

    public ListDataModel<Task> getDoneTasks() {
        /*List<Task> doneTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(doneTasksList);
        }
        for (Story story : storyList.getStoryList()) {
            doneTasksList.addAll(story.getDoneTasks());
        }
        doneTasks = new ListDataModel<Task>(doneTasksList);*/
        return doneTasks;
    }

    private String editTask(Task currentTask) {
        if (currentTask == null)
            return "";

        taskManager.setCurrentTask(currentTask);
        Story currentStory = storyManager.getCurrentStory();
        if (currentStory != currentTask.getStory()) {
            storyManager.setCurrentStory(currentTask.getStory());
        }
        return "/task/edit";
    }

    public String editToDoTask() {
        return editTask(toDoTasks.getRowData());
    }

    public String editDoneTask() {
        return editTask(doneTasks.getRowData());
    }

    public String editWorkingTask() {
        return editTask(workingTasks.getRowData());
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public SprintManager getSprintManager() {
        return sprintManager;
    }

    public void setSprintManager(SprintManager sprintManager) {
        this.sprintManager = sprintManager;
    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public void setStoryManager(StoryManagerImpl storyManager) {
        this.storyManager = storyManager;
    }

    public StoryList getStoryList() {
        return storyList;
    }

    public void setStoryList(StoryList storyList) {
        this.storyList = storyList;
    }

}
