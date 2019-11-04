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

<%
  String lotteryName = (String) request.getAttribute("lottery_name"); 
  String lotteryNumber = (String) request.getAttribute("lottery_number"); 
  String lotteryDate = (String) request.getAttribute("lottery_date"); 
%>

<html> 
  <body> 
    <center>

      <h2><fmt:message key="california_lottery"/></h2> 

      <fmt:message key="quick_pick_results">
        <fmt:param>
          <%= lotteryName%>
        </fmt:param> 
        <fmt:param>
          <%= lotteryDate%>
        </fmt:param> 
      </fmt:message>
      <br><br>
      <b><%= lotteryNumber%></b><br><br>

      <fmt:message key="play_again"/> &nbsp<a href="index.html"><fmt:message key="here"/></a>.

    </center>
  </body>
</html>
