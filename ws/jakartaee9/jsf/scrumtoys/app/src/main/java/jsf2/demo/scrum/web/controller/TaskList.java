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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.inject.Inject;
import jakarta.faces.model.DataModel;
import jakarta.faces.model.ListDataModel;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.Task;

/**
 * @author Eder Magalhaes
 */
@Named("taskList")
@ViewScoped
public class TaskList extends AbstractManager implements Serializable {

    @Inject
    private TaskManager taskManager;
    
    private DataModel<Task> tasks;
    private List<Task> taskList;
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    @Transactional
    public void init() {
        List<Task> temp = Collections.EMPTY_LIST;
        Story s = taskManager.getStory();
        if (s != null) {
            Query query = em.createNamedQuery("task.getByStory");
            query.setParameter("story", s);
            temp = (List<Task>) query.getResultList();
        }
        setTaskList(temp);        
    }
    
    public String edit() {
        return taskManager.edit(tasks.getRowData());
    }
    
    public String remove() {
        String result = taskManager.remove(tasks.getRowData());
        init();
        return result;
    }
    
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void setTasks(DataModel<Task> tasks) {
        this.tasks = tasks;
    }

    public DataModel<Task> getTasks() {
        this.tasks = new ListDataModel<Task>(taskList);
        return tasks;
    }
    
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    
}
