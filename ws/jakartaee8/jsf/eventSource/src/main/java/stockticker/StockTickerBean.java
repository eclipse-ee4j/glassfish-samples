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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ApplicationScoped;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationContext;
import org.glassfish.webcomm.api.WebCommunicationHandler;
import org.glassfish.webcomm.annotation.WebHandlerContext;

/**
 * StockTickerBean class
 */
@Named
@ApplicationScoped
public class StockTickerBean {

    @Inject @WebHandlerContext("/stockticker")
    private WebCommunicationContext wcc;
    private Timer timer = null;
    private Task task = null;
    private String symbols = "";
    private String openPrice = null;
    private Map <String, String>symbolOpenPrice = null;

    public StockTickerBean() {
    }

    public void getStockInfo() {
        synchronized(this) {
            if (symbolOpenPrice == null) {
                symbolOpenPrice = new HashMap<String, String>();
            }
            if (null == timer) {
                timer = new Timer();
                if (null == task) {
                    task = new Task();
                }
                timer.schedule(task, 0, 3 * 1000);
            }
        }
    }

    public void reset() {
        synchronized(this) {
            setSymbols("");
        }
    }

    public String getSymbols() {
        return symbols;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }
    
    // -- Message composition -------------------------------------------

    private String composeMessage() {
        String[] temp;
        temp = symbols.split(" ");
        StringBuilder sb = new StringBuilder();
        String openPrice = null;
        for (String t : temp) {
            String sym = t.trim();
            if (sym.length() > 0) {
                openPrice = symbolOpenPrice.get(sym);
                if (openPrice == null) {
                    openPrice = getTickerFromWebService(sym);
                    symbolOpenPrice.put(sym, openPrice);
                } 
                String price = getTickerFromWebService(sym);
                String chg = null;
                if (Double.valueOf(openPrice.trim()).doubleValue() < 
                    Double.valueOf(price.trim()).doubleValue()) {
                    chg = "up";
                } else if (Double.valueOf(openPrice.trim()).doubleValue() > 
                    Double.valueOf(price.trim()).doubleValue()) {
                    chg = "down";
                } else {
                    chg = "";
                }
                sb.append(sym).append(':').append(openPrice).
                    append(':').append(price).append(':').append(chg);
                sb.append(' ');
            }
        }
        if (sb.length() > 0) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    private String getTickerFromWebService(String symbol) {
        Random r = new Random();
        double q = symbol.hashCode() % 100 + r.nextFloat() * 5;
        return Double.toString(q).substring(0, 5);
    }
    
    class Task extends TimerTask {
        public void run() {
            for (WebCommunicationHandler wch : wcc.getHandlers()) {
                try {
                    String msg = composeMessage();
                    ((ServerSentEventHandler)wch).sendMessage(msg, "stock");
                } catch (IOException e) {
                    wch.close();
                }
            }
        }

    }
}
