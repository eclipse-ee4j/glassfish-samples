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

    <body>
        Test Http Method Omissions in Servlet
        <br><br>
        <form name="formPost" action="/http-method-omission/omissionservlet" method="POST">
            The Post Method is permitted only for javaee6user<br>
            <input type="submit" value="Try Post" />
        </form>
        <br><br>
        <form name="formGet" action="/http-method-omission/omissionservlet" method="GET">
            The Get method is denied access to all users <br>
            <input type="submit" value="Try Get" />
        </form>
        
    </body>
</html>
    
