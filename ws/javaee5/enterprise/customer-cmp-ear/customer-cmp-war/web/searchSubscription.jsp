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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collection" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="javax.ejb.ObjectNotFoundException" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Customer" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Subscription" %>
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.CustomerSessionLocal" %>
<%@ page import='java.util.*' %>
 
<html>
<head><title> <fmt:message key="cmp_demo_title"/> </title></head>
<body bgcolor="white">
<center>
<h2> <fmt:message key="cmp_demo_title"/> </h2>

<fmt:message key="search_by_title"/> :
<p>
    <form method="get" action="/customer-cmp-war/searchSubscription.jsp">
    <input type="text" name="searchText" size="25">
    <p>
    <input type="submit" value= <fmt:message key="search"/> >
    </form>

<%
String text = request.getParameter("searchText");

Subscription subscription = null;
if (text != null && !"".equals(text)) {
    try {
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
        CustomerSessionLocal custSession = (CustomerSessionLocal) o;

        subscription = custSession.searchForSubscription(text);

%>
<fmt:message key="results"/> : <p>
<%
if (subscription != null) {
%>
<%=subscription.getTitle()%> [<%=subscription.getType()%>] 
<p>
<fmt:message key="people_who_have_subscription"/> :
<p>
<%
Collection customers = subscription.getCustomers();
Iterator cust_iter = customers.iterator();

while(cust_iter.hasNext()){
  Customer customer = (Customer)cust_iter.next();
%>
  <a href="/customer-cmp-war/editCustomer.jsp?cid=<%=customer.getCustomerID()%>">
  <%=customer.getLastName()%>, <%=customer.getFirstName()%></a>
  <p>
<%
}
if (customers.size() == 0) {
%>
<fmt:message key="none"/> .
<%}%>
<p>
<%
}
%>
<%
    } catch(Exception e) {
        e.printStackTrace();
        out.println(e.toString());
    }
}
%>

<hr>
[<a href="/customer-cmp-war/index.html"><fmt:message key="home"/> </a>]
</center>
</body>
</html>
