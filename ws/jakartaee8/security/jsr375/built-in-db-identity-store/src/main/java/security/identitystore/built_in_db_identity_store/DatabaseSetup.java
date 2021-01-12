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

package security.identitystore.built_in_db_identity_store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

@Singleton
@Startup
public class DatabaseSetup {
    
    @Resource(lookup="java:comp/DefaultDataSource")	
    private DataSource dataSource;

    @Inject
    private Pbkdf2PasswordHash passwordHash;
    
    @PostConstruct
    public void init() {
        
        Map<String, String> parameters= new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);

        executeUpdate(dataSource, "CREATE TABLE caller(name VARCHAR(64) PRIMARY KEY, password VARCHAR(255))");
        executeUpdate(dataSource, "CREATE TABLE caller_groups(caller_name VARCHAR(64), group_name VARCHAR(64))");
        
        executeUpdate(dataSource, "INSERT INTO caller VALUES('Joe', '" + passwordHash.generate("secret1".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('Sam', '" + passwordHash.generate("secret2".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('Tom', '" + passwordHash.generate("secret2".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO caller VALUES('Sue', '" + passwordHash.generate("secret2".toCharArray()) + "')");
        
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Joe', 'foo')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Joe', 'bar')");
        
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Sam', 'foo')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Sam', 'bar')");
        
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Tom', 'foo')");
        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Sue', 'foo')");
    }

    /**
     * Drops the tables before instance is removed by the container.
     */
    @PreDestroy
    public void destroy() {
    	try {
    		executeUpdate(dataSource, "DROP TABLE caller");
    		executeUpdate(dataSource, "DROP TABLE caller_groups");
    	} catch (Exception e) {
    		// silently ignore, concerns in-memory database
    	}
    }

    /*
    Executes the SQL statement in this PreparedStatement object against the database it is pointing to.
     */
    private void executeUpdate(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
           throw new IllegalStateException(e);
        }
    }
    
}
