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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import jsf2.demo.scrum.model.entities.Sprint;
import jsf2.demo.scrum.model.entities.Story;

/**
 * @author Eder Magalhaes
 */
@Named("storyList")
@ViewScoped
public class StoryList extends AbstractManager implements Serializable{

    @Inject
    private StoryManager storyManager;

    private DataModel<Story> stories;
    private List<Story> storyList;
    
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void init() {
        List<Story> temp = Collections.EMPTY_LIST;
        Sprint s = storyManager.getSprint();
        if (s != null) {
            Query query = em.createNamedQuery("story.getBySprint");
            query.setParameter("sprint", s);
            temp = (List<Story>) query.getResultList();
        }
        setStoryList(temp);
    }

    public StoryManager getStoryManager() {
        return storyManager;
    }

    public void setStoryManager(StoryManager storyManager) {
        this.storyManager = storyManager;
    }

    public DataModel<Story> getStories() {
        this.stories = new ListDataModel<Story>(storyList);

        return stories;
    }

    public void setStories(DataModel<Story> stories) {
        this.stories = stories;
    }


    public List<Story> getStoryList() {
        return this.storyList;
    }

    public void setStoryList(List<Story> storyList) {
        this.storyList = storyList;
    }

    public String edit() {
        return storyManager.edit(stories.getRowData());
    }

    public String remove() {
        String result = storyManager.remove(stories.getRowData());
        init();
        return result;
    }

    public String showTasks() {
        return storyManager.showTasks(stories.getRowData());
    }
}
