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
import java.io.InputStream;
import java.util.Scanner;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;
@FacesValidator(value="FileValidator")
public class FileValidator implements Validator {
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        Part file = (Part) value;
        String text = "";
        
        try {
            InputStream is = file.getInputStream();
            text = new Scanner( is ).useDelimiter("\\A").next();
            // Do not accept an upload unless it contains the string
            // JSR-344
        } catch (Exception ex) {
            throw new ValidatorException(new FacesMessage("Invalid file"), ex);
        }
        if (!text.contains("scrumtoys")) {
            throw new ValidatorException(new FacesMessage("Invalid file.  File must contain special string"));
        }
        
    }
    
    
    
}
