<%--
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
--%>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
    </head>
    <body>

    <h1>Login</h1>
    
    <f:view>
        <h:messages style="color: red"
                    showDetail="true"/>
        <h:form id="login">
            <h:panelGrid columns="2" border="0">
                Username: <h:inputText id="username" 
                                       value="#{usermanager.username}"/>        
                Password: <h:inputSecret id="password"
                                         value="#{usermanager.password}"/>
            </h:panelGrid>
            <h:commandButton id="submit" 
                             type="submit"
                             value="Login"
                             action="#{usermanager.validateUser}"/>
            <br>
            <h:commandLink id="create"
                           value="Create New Account"
                           action="create"/>
        </h:form>
       
    </f:view>
    
    </body>
</html>
