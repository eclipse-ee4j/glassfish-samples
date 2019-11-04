<?xml version="1.0" ?>
<!--
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
-->
  <xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns0="http://hello.org/wsdl/HelloWorld" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <xsl:output method="xml" encoding="utf-8" undeclare-prefixes="no"/> 
    <xsl:template match="/">
    <xsl:text>
    <env:Envelope xmlns:env="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:enc="http://schemas.xmlsoap.org/soap/encoding/" xmlns:ns0="http://hello.org/wsdl/HelloWorld" env:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
<env:Body>
<ns0:sayHello>
<String_1 xsi:type="xsd:string">Satish!</String_1>
</ns0:sayHello>
</env:Body>
</env:Envelope>
    </xsl:text>
    </xsl:template>
    </xsl:stylesheet>
