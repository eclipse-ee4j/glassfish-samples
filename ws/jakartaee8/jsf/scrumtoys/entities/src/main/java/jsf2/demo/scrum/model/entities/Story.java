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

package jsf2.demo.scrum.model.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "stories", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "sprint_id"}))
@NamedQueries({
        @NamedQuery(name = "story.countByNameAndSprint", query = "select count(s) from Story as s where s.name = :name and s.sprint = :sprint and not(s = :currentStory)"),
        @NamedQuery(name = "story.new.countByNameAndSprint", query = "select count(s) from Story as s where s.name = :name and s.sprint = :sprint"),
        @NamedQuery(name = "story.getBySprint", query = "select s from Story as s where s.sprint = :sprint"),
        @NamedQuery(name = "story.remove.ByProject", query = "delete from Story as s where s.sprint.project = :project")
})
public class Story extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false)
    private String name;
    private int priority;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    private String acceptance;
    private int estimation;
    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "story", cascade = CascadeType.ALL)
    private List<UploadedFile> uploadedFiles;

    public List<UploadedFile> getUploadedFiles() {
        if (null == uploadedFiles) {
            uploadedFiles = new ArrayList<UploadedFile>();
        }
        return uploadedFiles;
    }
    
    public void removeUploadedFile(UploadedFile toRemove) {
        if (null != uploadedFiles) {
            uploadedFiles.remove(toRemove);
        }
    }
    
    public Story() {
        this.startDate = new Date();
    }

    public Story(String name) {
        this();
        this.name = name;
    }

    public Story(String name, Sprint sprint) {
        this(name);
        this.name = name;
        if (sprint != null) {
            sprint.addStory(this);
        }
    }

    public String getAcceptance() {
        return acceptance;
    }

    public void setAcceptance(String acceptance) {
        this.acceptance = acceptance;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getEstimation() {
        return estimation;
    }

    public void setEstimation(int estimation) {
        this.estimation = estimation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<Task> getTasks() {
        return (tasks != null) ? Collections.unmodifiableList(tasks) : Collections.<Task>emptyList();
    }

    public List<Task> getDoneTasks() {
        return Collections.unmodifiableList(getTasks(TaskStatus.DONE));
    }

    public List<Task> getWorkingTasks() {
        return Collections.unmodifiableList(getTasks(TaskStatus.WORKING));
    }

    public List<Task> getTodoTasks() {
        return Collections.unmodifiableList(getTasks(TaskStatus.TODO));
    }

    private List<Task> getTasks(TaskStatus status) {
        List<Task> result = new LinkedList<Task>();
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
                if (task != null && status.equals(task.getStatus())) {
                    result.add(task);
                }
            }
        }
        return result;
    }

    public boolean addTask(Task task) {
        if (tasks == null) {
            tasks = new LinkedList<Task>();
        }
        if (task != null && !tasks.contains(task)) {
            tasks.add(task);
            task.setStory(this);
            return true;
        }
        return false;
    }

    public boolean removeTask(Task task) {
        return (tasks != null && !tasks.isEmpty() && tasks.remove(task));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Story other = (Story) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return !(this.sprint != other.sprint
                 && (this.sprint == null || !this.sprint.equals(other.sprint)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 23 * hash + (this.sprint != null ? this.sprint.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Story[name=" + name + ",startDate=" + startDate + ",sprint=" + sprint + "]";
    }
}
