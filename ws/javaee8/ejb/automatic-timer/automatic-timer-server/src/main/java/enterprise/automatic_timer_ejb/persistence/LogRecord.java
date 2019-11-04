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


 * LogRecord.java
 * @author Marina Vatkina
 */

package enterprise.automatic_timer_ejb.persistence;

import javax.persistence.*;

//name defaults to the unqualified entity class name.        
//default access is property.
@Entity
@NamedQueries({
    @NamedQuery(name = "LogRecord.countLoggedTimeouts", query = "select count(l) from LogRecord l where l.record NOT LIKE \"Canceling timer%\""),
    @NamedQuery(name = "LogRecord.findAllRecords", query = "select l.record from LogRecord l")
})
public class LogRecord {
    
    @Id 
    @GeneratedValue
    private int id;
    private String record;
    
    public LogRecord(String record) {
        setRecord(record);
    }
    
    public LogRecord() {
    }
    
    public String getRecord() {
        return record;
    }
    public void setRecord(String record) {
        this.record = record;
    }
    
    
}
