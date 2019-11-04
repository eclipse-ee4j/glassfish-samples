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
<%@ page import="javax.ejb.ObjectNotFoundException" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="enterprise.customer_cmp_ejb.persistence.Address" %>
<%@ page import="enterprise.customer_cmp_ejb.ejb.session.*" %>
<%@ page import='java.util.*' %>
 
<html>

<head><title><fmt:message key="cmp_demo_title"/></title></head>
<body bgcolor="white">
<center>
<h2><fmt:message key="cmp_demo_title"/></h2>

<fmt:message key="search_address_by_id"/>:
<p>
    <form method="get" action="/customer-cmp-war/searchAddress.jsp">
    <input type="text" name="searchText" size="25">
    <p>
    <input type="submit" value=<fmt:message key="search"/> >
    </form>

<%
String text = request.getParameter("searchText");

Address address = null;
if (text != null && !"".equals(text)) {
    try {
        InitialContext ic = new InitialContext();
        Object o = ic.lookup("java:comp/env/CustomerSessionLocal");
        CustomerSessionLocal custSession = (CustomerSessionLocal) o;

        address = custSession.searchForAddress(text);

%>
<fmt:message key="results"/> : <p>
<%
if (address != null) {
%>
<fmt:message key="address"/>  [<%=address.getAddressID()%>] : 
<%=address.getStreet()%>, <%=address.getCity()%> 
<p>
<%
} else {
%>
<fmt:message key="address"/> [<%=text%>] <fmt:message key="not_found"/>.
<%
}
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
