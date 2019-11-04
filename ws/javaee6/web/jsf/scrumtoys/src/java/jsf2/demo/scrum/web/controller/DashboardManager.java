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

import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.Task;

import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;


@ManagedBean(name = "dashboardManager")
@ViewScoped
public class DashboardManager extends AbstractManager implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{taskManager}")
    private TaskManager taskManager;
    @ManagedProperty("#{sprintManager}")
    private SprintManager sprintManager;
    @ManagedProperty("#{storyManager}")
    private StoryManager storyManager;


    private ListDataModel<Task> toDoTasks;
    private ListDataModel<Task> workingTasks;
    private ListDataModel<Task> doneTasks;

    @PreDestroy
    public void destroy() {
        toDoTasks = null;
        workingTasks = null;
        doneTasks = null;
        FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("dashboardManager");
    }

    public Sprint getSprint() {
        return getSprintManager().getCurrentSprint();
    }

    public void setSprint(Sprint sprint) {
        this.getSprintManager().setCurrentSprint(sprint);
    }

    public DataModel<Story> getStories() {
        return storyManager.getStories();
    }


    public void setStories(DataModel<Story> stories) {
        storyManager.setStories(stories);
    }

    public ListDataModel<Task> getToDoTasks() {
        List<Task> toDoTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(toDoTasksList);
        }
        for (Story story : storyManager.getStoryList()) {
            toDoTasksList.addAll(story.getTodoTasks());
        }
        toDoTasks = new ListDataModel<Task>(toDoTasksList);
        return toDoTasks;
    }

    public ListDataModel<Task> getWorkingTasks() {
        List<Task> workingTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(workingTasksList);
        }
        for (Story story : storyManager.getStoryList()) {
            workingTasksList.addAll(story.getWorkingTasks());
        }
        workingTasks = new ListDataModel<Task>(workingTasksList);
        return workingTasks;
    }

    public ListDataModel<Task> getDoneTasks() {
        List<Task> doneTasksList = new ArrayList<Task>();
        if (sprintManager.getCurrentSprint() == null) {
            return new ListDataModel<Task>(doneTasksList);
        }
        for (Story story : storyManager.getStoryList()) {
            doneTasksList.addAll(story.getDoneTasks());
        }
        doneTasks = new ListDataModel<Task>(doneTasksList);
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

    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

}
