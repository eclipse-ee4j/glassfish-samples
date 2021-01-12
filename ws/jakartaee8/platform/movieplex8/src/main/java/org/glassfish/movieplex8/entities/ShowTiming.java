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
package org.glassfish.movieplex8.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ArunGupta
 */
@Entity
@Table(name = "SHOW_TIMING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ShowTiming.findAll", query = "SELECT s FROM ShowTiming s"),
    @NamedQuery(name = "ShowTiming.findById", query = "SELECT s FROM ShowTiming s WHERE s.id = :id"),
    @NamedQuery(name = "ShowTiming.findByMovieAndTimingId", query = "SELECT s FROM ShowTiming s WHERE s.movieId.id = :movieId AND s.timingId.id = :timingId"),
    @NamedQuery(name = "ShowTiming.findByDay", query = "SELECT s FROM ShowTiming s WHERE s.day = :day")})
public class ShowTiming implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    private Integer id;
    
    @NotNull
    private int day;
    
    @JoinColumn(name = "TIMING_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Timeslot timingId;
    
    @JoinColumn(name = "THEATER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Theater theaterId;
    
    @JoinColumn(name = "MOVIE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Movie movieId;
    
    public ShowTiming() {
    }

    public ShowTiming(Integer id) {
        this.id = id;
    }

    public ShowTiming(Integer id, int day) {
        this.id = id;
        this.day = day;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Timeslot getTimingId() {
        return timingId;
    }

    public void setTimingId(Timeslot timingId) {
        this.timingId = timingId;
    }

    public Theater getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(Theater theaterId) {
        this.theaterId = theaterId;
    }

    public Movie getMovieId() {
        return movieId;
    }

    public void setMovieId(Movie movieId) {
        this.movieId = movieId;
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
        if (!(object instanceof ShowTiming)) {
            return false;
        }
        ShowTiming other = (ShowTiming) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return movieId.getName() + ", " + timingId.getStartTime();
    }
}
