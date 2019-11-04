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

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Web Beans Servlet Injection</title>
        <script type="text/javascript" src="resources/ajax.js">
        </script>
        <link type="text/css" rel="stylesheet" href="/webbeans-servlet/resources/stylesheet.css" />
    </head>
    <body>
        <form name="myForm" method="POST" action="LoginServlet">
            <table class="title-panel">
                <tbody>
                    <tr>
                        <td><span class="title-panel-text">Login Servlet</span></td>
                    </tr>
                    <tr>
                        <td><span class="title-panel-subtext">Powered By Servlet 3.0 and Web Beans</span></td>
                    </tr>
                </tbody>
            </table>
            <table height="30" style="font-size: 16px">
              <tr>
                <td>Enter any value for user name and password.</td>
              </tr>
            </table>
            <table style="font-size: 16px">
              <tr>
                <td style="color:red">*</td>
                <td>Denotes required entry.</td>
              </tr>
            </table>
            <table height="30">
            <table border="1" style="font-size: 18px">
              <tr>
                <td>User Name:</td>
                <td><input type="text" name="username" id="username" /></td>
                <td style="color:red">*</td>
              </tr>
              <tr>
                <td>Password:</td>
                <td><input type="password" name="password" id="password" /></td>
                <td style="color:red">*</td>
              </tr>
            </table>
            <table border="1">
              <tr>
                <td colspan="2"><input type="button" value="Submit"  onclick="ajaxFunction();" /></td>
                <td colspan="2"><input type="button" value="Reset"  onclick="resetFunction();" /></td>
              </tr> 
            </table>
            </table>
            <table height="20">
              <tr>
                <td><div id="message" style="color:red;font-size: 14px"></td>
              </tr>
            </table>

         </form>
    </body>
</html>
