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

package basicajaxtest;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Assert;
import org.junit.Test;

public class BasicAjaxTest {

    @Test
    public void homePage() throws Exception {
        final WebClient webClient = new WebClient();
        final HtmlPage page = webClient.getPage("http://localhost:8080/basic-ajax/home.jsf");
        Assert.assertEquals("JavaServer Faces 2.0 Basic Ajax Demo", page.getTitleText());
    }

    @Test
    public void checkSimpleCount() throws Exception {
        final WebClient webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        final HtmlPage homepage = webClient.getPage("http://localhost:8080/basic-ajax/home.jsf");
        HtmlTable table = (HtmlTable) homepage.getElementById("demo-table");
        HtmlTableCell cell = table.getCellAt(1,2);
        HtmlAnchor link = (HtmlAnchor) cell.getHtmlElementsByTagName("a").get(0);
        HtmlPage demopage = link.click();
        Assert.assertEquals("0",demopage.getHtmlElementById("out1").getTextContent());
        HtmlSubmitInput button = demopage.getHtmlElementById("button1");
        demopage = button.click();
        Assert.assertEquals("1",demopage.getHtmlElementById("out1").getTextContent());
    }

    @Test
    public void checkSecondEcho() throws Exception {
        final WebClient webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        final HtmlPage homepage = webClient.getPage("http://localhost:8080/basic-ajax/home.jsf");
        HtmlTable table = (HtmlTable) homepage.getElementById("demo-table");
        HtmlTableCell cell = table.getCellAt(4,2);
        HtmlAnchor link =  (HtmlAnchor) cell.getHtmlElementsByTagName("a").get(0);
        HtmlPage demopage = link.click();
        Assert.assertEquals("hello",demopage.getHtmlElementById("form1:out1").getTextContent());
        HtmlTextInput input = demopage.getHtmlElementById("form1:in1");
        input.setValueAttribute("hello test");
        HtmlButtonInput button = demopage.getHtmlElementById("form1:button1");
        demopage = button.click();
        Assert.assertEquals("hello test",demopage.getHtmlElementById("form1:out1").getTextContent());
    }
}
