/*
 * Copyright (c) 2019, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package stockticker;

import java.io.IOException;
import org.glassfish.webcomm.api.ServerSentEventHandler;
import org.glassfish.webcomm.api.WebCommunicationClient;
import org.glassfish.webcomm.annotation.WebHandler;

/**
 * StockTickerHandler class
 */
@WebHandler("/stockticker")
public class StockTickerHandler extends ServerSentEventHandler<String> {

    @Override
    public void onConnect(WebCommunicationClient client) {
        if (acceptClientConnection(client)) {
            super.onConnect(client);
        } else {
            throw new RuntimeException("Connection rejected!");
        }
    }

    @Override
    public void sendMessage(String data) throws IOException {
        super.sendMessage(data.toUpperCase());
    }

    @Override
    public void sendMessage(String data, String eventName) throws IOException {
        super.sendMessage(data.toUpperCase(), eventName);
    }

    private boolean acceptClientConnection(WebCommunicationClient client) {
        String remoteAddr = client.getRemoteAddr();
        return true;        // TODO: Filter using remoteAddr
    }
}
