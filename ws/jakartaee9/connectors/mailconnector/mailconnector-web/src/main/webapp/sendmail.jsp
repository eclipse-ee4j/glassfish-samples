<%--

    Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.

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
<title><fmt:message key="sendmail.jsp.title"/></title>
</head>
<body bgcolor="white">

<p><fmt:message key="sendmail.jsp.overview"/></p>
<ul>
<li><fmt:message key="sendmail.jsp.defaultconfig"/></li>
<li><fmt:message key="sendmail.jsp.applicationlogic"/></li>
<li><fmt:message key="sendmail.jsp.allfields"/></li>
</ul>

<%
    if (session.getAttribute("mailto") == null)
	session.setAttribute("mailto", "joe@localhost");
    if (session.getAttribute("mailfrom") == null)
	session.setAttribute("mailfrom", "tom@localhost");
    if (session.getAttribute("mailsubject") == null)
	session.setAttribute("mailsubject", "What's up");
%>

<form method="POST" action="mail">
<table>

  <tr>
    <th align="center" colspan="2">
      <fmt:message key="sendmail.jsp.entermessage"/>
    </th>
  </tr>

  <tr>
    <th align="right"><fmt:message key="sendmail.jsp.from"/></th>
    <td align="left">
      <input type="text" name="mailfrom" size="60" value="<%= session.getAttribute("mailfrom") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="sendmail.jsp.to"/></th>
    <td align="left">
      <input type="text" name="mailto" size="60" value="<%= session.getAttribute("mailto") %>">
    </td>
  </tr>

  <tr>
    <th align="right"><fmt:message key="sendmail.jsp.subject"/></th>
    <td align="left">
      <input type="text" name="mailsubject" size="60" value="<%= session.getAttribute("mailsubject") %>">
    </td>
  </tr>

  <tr>
    <td colspan="2">
      <textarea name="mailcontent" rows="10" cols="80">
      </textarea>
    </td> 
  </tr>

  <tr>
    <td align="right">
      <input type="submit" value="<fmt:message key="sendmail.jsp.sendbutton"/>">
    </td>
    <td align="left">
      <input type="reset" value="<fmt:message key="sendmail.jsp.resetbutton"/>">
    </td>
  </tr>

</table>
</form>

</body>
</html>
