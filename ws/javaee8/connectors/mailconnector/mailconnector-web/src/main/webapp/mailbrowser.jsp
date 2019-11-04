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
<fmt:setBundle basename="LogStrings"/>

<html>
<head>
<title><fmt:message key="mailbrowser.jsp.title"/></title>
</head>
<body bgcolor="white">

<p><fmt:message key="mailbrowser.jsp.overview"/></p>
<p><fmt:message key="mailbrowser.jsp.note"/>
<ul>
<li><fmt:message key="mailbrowser.jsp.defaultconfig"/></li>
<li><fmt:message key="mailbrowser.jsp.allfields"/></li>
</ul>
</p>

<%
    if (session.getAttribute("folder") == null)
	session.setAttribute("folder", "Inbox");
    if (session.getAttribute("server") == null)
	session.setAttribute("server", "ServerName");
    if (session.getAttribute("username") == null)
	session.setAttribute("username", "UserName");
    if (session.getAttribute("password") == null)
	session.setAttribute("password", "");
%>

<form method="POST" action="browse">
<table>

  <tr>
    <th align="center" colspan="2">
      <fmt:message key="mailbrowser.jsp.entermessage"/>
    </th>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.folder"/></th>
    <td align="left">
      <input type="text" name="folder" size="60" value="<%= session.getAttribute("folder") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.server"/></th>
    <td align="left">
      <input type="text" name="server" size="60" value="<%= session.getAttribute("server") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.username"/></th>
    <td align="left">
      <input type="text" name="username" size="60" value="<%= session.getAttribute("username") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.password"/></th>
    <td align="left">
      <input type="password" name="password" size="60" value="<%= session.getAttribute("password") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="mailbrowser.jsp.protocol"/></th>
    <td align="left">
      <input type="text" name="protocol" size="60" value="IMAP">
    </td>
  </tr>

  <tr>
    <td align="right">
      <input type="submit" value="<fmt:message key="mailbrowser.jsp.sendbutton"/>">
    </td>
    <td align="left">
      <input type="reset" value="<fmt:message key="mailbrowser.jsp.resetbutton"/>">
    </td>
  </tr>

</table>
</form>

</body>
</html>
