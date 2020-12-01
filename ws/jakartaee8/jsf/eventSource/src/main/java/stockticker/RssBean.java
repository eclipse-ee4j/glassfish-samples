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
package stockticker;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.util.*;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.inject.Inject;
import javax.inject.Named;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebCommunicationHandler;
import org.glassfish.webcomm.annotation.WebHandlerContext;

/**
 * RssBean class
 */
@Named
@ApplicationScoped
public class RssBean {

    @Inject @WebHandlerContext("/stockticker")
    private WebCommunicationContext wcc;
    private Timer timer = null;
    private Task task = null;
    private String symbols = "";

    public RssBean() {

    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public void getStockNews() {
        if (symbols.equals("")) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Application application = facesContext.getApplication();
            ValueBinding binding = application.createValueBinding("#{stockTickerBean}");
            StockTickerBean stockTickerBean = (StockTickerBean)binding.getValue(facesContext);
            this.symbols = stockTickerBean.getSymbols();
        }
        synchronized(this) {
            if (null == timer) {
                timer = new Timer();
                if (null == task) {
                    task = new Task();
                }
                timer.schedule(task, 0, 5 * 1000);
            }
        }
    }

    // -- Message composition -------------------------------------------

    private String composeMessage() {
        return getRSSFeeds(symbols);
    }

    private String getRSSFeeds(String symbols) {
        String rssURL = "http://finance.yahoo.com/rss/headline?s=";
        String feeds = "";
        String[] temp;
        temp = symbols.split(" ");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String t : temp) {
            String sym = t.trim();
            if (sym.length() > 0) {
                if (!first) {
                    rssURL += ",";
                }
                rssURL += sym;
                first = false;
            }
        }
        try {
            URL feedURL = new URL(rssURL);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedURL));
            List list = feed.getEntries();
            if(list.size() > 0)    {
                Collections.shuffle(list);
                for (int i = 0; i < list.size(); i++ ) {
                    String container = "<p class=\"RSSlist\">";
                    sb.append(container);
                    String title = ((SyndEntry)list.get(i)).getTitle();
                    sb.append(title);
                    String endContainer = "</p>*****";
                    sb.append(endContainer);
                }
            }
        } catch(Exception e){}

        finally {
            return sb.toString();
        }
    }

    class Task extends TimerTask {
        public void run() {
            for (WebCommunicationHandler wch : wcc.getHandlers()) {
                try {
                    String msg = composeMessage();
System.err.println("SENDING MSG:"+msg);
                    ((ServerSentEventHandler)wch).sendMessage(msg, "rss");
                } catch (IOException e) {
                    wch.close();
                }
            }
        }

    }
}
