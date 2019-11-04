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
package org.glassfish.movieplex7.client;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.movieplex7.entities.Movie;
import org.glassfish.movieplex7.json.MovieWriter;

/**
 * @author Arun Gupta
 */
@Named
@SessionScoped
public class MovieClientBean implements Serializable {

    @Inject
    MovieBackingBean bean;
    
    Client client;
    WebTarget target;

    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        target = client
                .target("http://localhost:8080/movieplex7/webresources/movie/");
    }

    @PreDestroy
    public void destroy() {
        client.close();
    }

    public Movie[] getMovies() {
        return target
                .request()
                .get(Movie[].class);
    }

    public Movie getMovie() {
        Movie m = target
                .path("{movie}")
                .resolveTemplate("movie", bean.getMovieId())
                .request()
                .get(Movie.class);
        return m;
    }

    public Movie getMovieJson() {
        Movie m = target
                .path("{movie}")
                .resolveTemplate("movie", bean.getMovieId())
                .request(MediaType.APPLICATION_JSON)
                .get(Movie.class);
        return m;
    }

    public void addMovie() {
        Movie m = new Movie();
        m.setId(bean.getMovieId());
        m.setName(bean.getMovieName());
        m.setActors(bean.getActors());
        target
                .register(MovieWriter.class)
                .request()
                .post(Entity.entity(m, MediaType.APPLICATION_JSON));
    }

    @Transactional
    public void deleteMovie() {
        target
                .path("{movieId}")
                .resolveTemplate("movieId", bean.getMovieId())
                .request()
                .delete();
    }
}
