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

package jsf2.demos.scrum.manageStoryAttachments;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.UploadedFile;
import jsf2.demo.scrum.web.controller.StoryManager;

/**
 *
 * @author Eder Magalhaes
 */
@Named("uploadedFileList")
@ViewScoped
public class UploadedFileList implements Serializable {
    
    @Inject StoryManager storyManager;
    
    private DataModel<UploadedFile> uploadedFiles;
    
    private List<UploadedFile> uploadedFileList;
    
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    @Transactional
    public void init() {
        List<UploadedFile> result = storyManager.getCurrentStory().getUploadedFiles();
        setUploadedFileList(result);
    }
    
    public DataModel<UploadedFile> getUploadedFiles() {
        this.uploadedFiles = new ListDataModel<UploadedFile>(uploadedFileList);
        return this.uploadedFiles;
    }

    public List<UploadedFile> getUploadedFileList() {
        return uploadedFileList;
    }
    
    public void setUploadedFiles(DataModel<UploadedFile> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }

    public void setUploadedFileList(List<UploadedFile> uploadedFileList) {
        this.uploadedFileList = uploadedFileList;
    }
    
    @Transactional
    public String remove() {
        Story managedStory = em.find(Story.class, storyManager.getCurrentStory().getId());
        UploadedFile managedFile = em.find(UploadedFile.class, uploadedFiles.getRowData().getId());
        
        managedStory.removeUploadedFile(managedFile);
        managedFile.setStory(null);
        storyManager.setCurrentStory(managedStory);
        
        em.remove(managedFile);
        
        return "show";
        
    }

}
