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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<fmt:setBundle basename="LocalStrings"/>
<%@ page language="java" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Customer" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Subscription" %>
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.*" %>
<%@ page import='java.util.*' %>
 
<html>

<head><title><fmt:message key="cmp_demo_title"/> </title></head>

<body bgcolor="white">
<center>
<h2><fmt:message key="cmp_demo_title"/> </h2>

<fmt:message key="create_customer"/> :
<p>
<form method="post" action="/customer-cmp-war/createCustomer.jsp">
<table border=10>
  <tr>
    <td><fmt:message key="customer_id"/> : </td>
    <td><input type="text" name="id" size="11" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="first_name"/> : </td>
    <td><input type="text" name="firstName" size="25" value=""></td>
  </tr>
  <tr>
    <td><fmt:message key="last_name"/> : </td>
    <td><input type="text" name="lastName" size="25" value=""></td>
  </tr>
</table>
<p>
<input type="submit" name="submit" value="Submit">
<p>
</form>

<%
String id = request.getParameter("id");
String firstName = request.getParameter("firstName");
String lastName = request.getParameter("lastName");

if (id != null && !"".equals(id)) {
    try {
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
        CustomerSessionLocal custSession = (CustomerSessionLocal) o;

        Customer customer = new Customer(id, firstName, lastName);
        custSession.persist(customer);
%>
<fmt:message key="new_customer"/> : 
<a href = "/customer-cmp-war/editCustomer.jsp?cid=<%=id%>">
<%=customer.getLastName()%>, 
<%=customer.getFirstName()%>
</a>
<fmt:message key="created"/> . 
</p>
<a href = "/customer-cmp-war/searchCustomer.jsp">SEARCH</a></p>
<!--<fmt:message key="create_customer_failed"/>-->
<%
    } catch(Exception e) {
        e.printStackTrace();
        out.println("Create Customer Failed : " + e.toString()); 
    } 
}
%>

<hr>
[<a href="/customer-cmp-war/index.html"><fmt:message key="home"/> </a>]
</center>
</body>
</html>
