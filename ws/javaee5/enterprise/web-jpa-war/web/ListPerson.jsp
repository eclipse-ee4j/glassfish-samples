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
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>List Of Persons</title>
    </head>
    <body>

    <h1>List of Persons currently in Database</h1>
    
<table id="personListTable" border="3">
<tr >
    <th bgcolor=>ID</th>
    <th bgcolor=>FirstName</th>
    <th bgcolor=>LastName</th>
</tr>
<c:forEach var="person" begin="0" items="${requestScope.personList}">
<tr>
    <td>${person.id}&nbsp;&nbsp;</td> 
    <td>${person.firstName}&nbsp;&nbsp;</td> 
    <td>${person.lastName}&nbsp;&nbsp;</td> 
</tr> 

</c:forEach>

</table>
<a href="CreatePerson.jsp"><strong>Create a Person Record</strong></a>
</body>
</html>
