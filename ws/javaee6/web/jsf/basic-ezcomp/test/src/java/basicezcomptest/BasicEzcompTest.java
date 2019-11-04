/*
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
*/

package basicezcomptest;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Assert;
import org.junit.Test;

public class BasicEzcompTest {

    @Test
    public void homePage() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage("http://localhost:8080/basic-ezcomp/index.jsf");
        Assert.assertEquals("JavaServer Faces 2.0 Composite Component Demo", page.getTitleText());
    }

    @Test
    public void checkInPage() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage homepage = webClient.getPage("http://localhost:8080/basic-ezcomp/index.jsf");
        HtmlTable table = (HtmlTable) homepage.getElementById("demo-table");
        HtmlTableCell cell = table.getCellAt(1,2);
        HtmlAnchor link = (HtmlAnchor) cell.getHtmlElementsByTagName("a").get(0);
        HtmlPage demopage = link.click();
        HtmlElement div = demopage.getHtmlElementById("cc");
        HtmlElement span = div.getHtmlElementsByTagName("span").get(0);
        Assert.assertEquals("Test Value",span.getTextContent());
    }

    

}
