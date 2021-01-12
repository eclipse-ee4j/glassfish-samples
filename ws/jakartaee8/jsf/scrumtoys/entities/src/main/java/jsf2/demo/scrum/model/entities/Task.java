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
import java.util.Date;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "tasks", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "story_id"}))
@NamedQueries({
    @NamedQuery(name = "task.countByNameAndStory", query = "select count(t) from Task as t where t.name = :name and t.story = :story and not(t = :currentTask)"),
    @NamedQuery(name = "task.getByStory", query = "select t from Task as t where t.story = :story"),
    @NamedQuery(name = "task.new.countByNameAndStory", query = "select count(t) from Task as t where t.name = :name and t.story = :story"),
    @NamedQuery(name = "task.getByStatusAndSprint", query = "select t from Task as t where t.status = :status and t.story.sprint = :sprint"),
    @NamedQuery(name = "task.remove.ByProject", query = "delete from Task as t where t.story.sprint.project = :project")
})
public class Task extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;

    @Enumerated(EnumType.ORDINAL)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "story_id")
    private Story story;

    public Task() {
        this.status = TaskStatus.TODO;
    }

    public Task(String name) {
        this();
        this.name = name;
    }

    public Task(String name, Story story) {
        this(name);
        if (story != null) {
            story.addTask(this);
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        changeTaskStatus(this.startDate, endDate);

    }

    protected void changeTaskStatus(Date startDate, Date endDate) {
        if (endDate != null) {
            this.setStatus(TaskStatus.DONE);
        }
        if (endDate == null && this.startDate != null) {
            this.setStatus(TaskStatus.WORKING);
        }
        if (endDate == null && this.startDate == null) {
            this.setStatus(TaskStatus.TODO);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
        changeTaskStatus(startDate, this.endDate);
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    public String getStatusKeyI18n() {
        return "task.show.table.header.status."+status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Task other = (Task) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return !(this.story != other.story
                 && (this.story == null || !this.story.equals(other.story)));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 83 * hash + (this.story != null ? this.story.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Task[name=" + name + ",startDate=" + startDate + ",story=" + story + "]";
    }
}
