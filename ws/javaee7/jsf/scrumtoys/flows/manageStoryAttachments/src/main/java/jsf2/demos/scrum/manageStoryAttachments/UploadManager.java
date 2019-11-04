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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.flow.FlowScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import jsf2.demo.scrum.model.entities.Story;
import jsf2.demo.scrum.model.entities.UploadedFile;
import jsf2.demo.scrum.web.controller.StoryManager;

@Named
@FlowScoped(definingDocumentId="manageStoryAttachments", value="manageStoryAttachments")
public class UploadManager implements Serializable {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private StoryManager storyManager;
    
    private Part uploadedFile;

    public Part getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(Part part) {
        this.uploadedFile = part;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            BufferedInputStream bits = new BufferedInputStream(part.getInputStream());
            byte [] buffer = new byte[8192];
            int num = 0;
            while (-1 != (num = bits.read(buffer))) {
                baos.write(buffer, 0, num);
            }
            
            UploadedFile entity = new UploadedFile();
            Story currentStory = storyManager.getCurrentStory();
            entity.setStory(currentStory);
            entity.setFileName(part.getName());
            entity.setBytes(baos.toByteArray());
            currentStory.getUploadedFiles().add(entity);

        } catch (Exception e) {
           Logger.getLogger (UploadManager.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException ex) {
                    Logger.getLogger (UploadManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        

    }
    
    
    
    
}
