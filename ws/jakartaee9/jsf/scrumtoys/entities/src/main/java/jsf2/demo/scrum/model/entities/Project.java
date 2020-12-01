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

package jsf2.demo.scrum.model.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * @author Dr. Spock (spock at dev.java.net)
 */
@Entity
@Table(name = "projects")
@NamedQueries({@NamedQuery(name = "project.getAll", query = "select p from Project as p"),
        @NamedQuery(name = "project.getAllOpen", query = "select p from Project as p where p.endDate is null"),
        @NamedQuery(name = "project.countByName", query = "select count(p) from Project as p where p.name = :name and not(p = :currentProject)"),
        @NamedQuery(name = "project.new.countByName", query = "select count(p) from Project as p where p.name = :name")})
public class Project extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, unique = true)
    private String name;
    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "end_date")
    private Date endDate;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Sprint> sprints;

    public Project() {
        this.startDate = new Date();
    }

    public Project(String name) {
        this();
        this.name = name;
    }

    public Project(String name, Date startDate) {
        this(name);
        this.startDate = startDate;
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
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<Sprint> getSprints() {
        return (sprints != null) ? Collections.unmodifiableList(sprints) : Collections.<Sprint>emptyList();
    }

    public boolean addSprint(Sprint sprint) {
        if (sprints == null) {
            sprints = new LinkedList<Sprint>();
        }
        if (sprint != null && !sprints.contains(sprint)) {
            sprints.add(sprint);
            sprint.setProject(this);
            return true;
        }
        return false;
    }

    public boolean removeSpring(Sprint sprint) {
        return (sprints != null
                   && !sprints.isEmpty()
                   && sprints.remove(sprint));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Project other = (Project) obj;
        return !((this.name == null)
                 ? (other.name != null)
                 : !this.name.equals(other.name));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Project[name=" + name + ",startDate=" + startDate + ",endDate=" + endDate + "]";
    }
}
