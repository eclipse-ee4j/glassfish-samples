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
package org.glassfish.movieplex7.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Arun Gupta
 */
@Entity
@Table(name = "THEATER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Theater.findAll", query = "SELECT t FROM Theater t"),
    @NamedQuery(name = "Theater.findById", query = "SELECT t FROM Theater t WHERE t.id = :id"),
    @NamedQuery(name = "Theater.findByCapacity", query = "SELECT t FROM Theater t WHERE t.capacity = :capacity")})
public class Theater implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    private Integer id;
    
    @NotNull
    private int capacity;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "theaterId")
    private Collection<ShowTiming> showTimingCollection;

    public Theater() {
    }

    public Theater(Integer id) {
        this.id = id;
    }

    public Theater(Integer id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @XmlTransient
    public Collection<ShowTiming> getShowTimingCollection() {
        return showTimingCollection;
    }

    public void setShowTimingCollection(Collection<ShowTiming> showTimingCollection) {
        this.showTimingCollection = showTimingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Theater)) {
            return false;
        }
        Theater other = (Theater) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "foo.Theater[ id=" + id + " ]";
    }
    
}
