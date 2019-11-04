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

<html>
    <head>
        <title>Test Web Security Annotations on a Servlet</title>
    </head>
    <body>
        <br>
        <form name="formGet" action="/web-security-annotation/annotate" method="GET">
            The Get Method is permitted only for javaee6user (@RolesAllowed)<br>
            <input type="submit" value="Try Get" />
        </form>
        <br><br>
        <form name="formPost" action="/web-security-annotation/annotate" method="POST">
            The Post method is denied access to all users (@DenyAll)<br>
            <input type="submit" value="Try Post" />
        </form>
        
    </body>
</html>
    
