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
package org.glassfish.movieplex7.booking;

import java.util.List;
import java.util.StringTokenizer;
import javax.faces.flow.FlowScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.glassfish.movieplex7.entities.Movie;
import org.glassfish.movieplex7.entities.ShowTiming;

/**
 * @author Arun Gupta
 */
@Named
@FlowScoped("booking")
public class Booking {

    int movieId;
    String startTime;
    int startTimeId;
    
    @PersistenceContext
    EntityManager em;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        try {
            return em.createNamedQuery("Movie.findById", Movie.class).setParameter("id", movieId).getSingleResult().getName();
        } catch (NoResultException e) {
            return "";
        }
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        StringTokenizer tokens = new StringTokenizer(startTime, ",");
        startTimeId = Integer.parseInt(tokens.nextToken());
        this.startTime = tokens.nextToken();
    }

    public int getStartTimeId() {
        return startTimeId;
    }
    
    public String getName() {
        return this.getClass().getSimpleName();
    }
    
    public String getTheater() {
        // for a movie and show
        try {
            // Always return the first theater
            List<ShowTiming> list = em.createNamedQuery("ShowTiming.findByMovieAndTimingId", ShowTiming.class)
                    .setParameter("movieId", movieId)
                    .setParameter("timingId", startTimeId)
                    .getResultList();
            if (list.isEmpty())
                return "none";
            
            return list
                    .get(0)
                    .getTheaterId()
                    .getId().toString();
        } catch (NoResultException e) {
            return "none";
        }
    }
}
