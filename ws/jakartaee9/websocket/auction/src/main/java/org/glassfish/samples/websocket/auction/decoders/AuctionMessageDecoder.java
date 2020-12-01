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

package org.glassfish.samples.websocket.auction.decoders;

import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;

import org.glassfish.samples.websocket.auction.message.AuctionMessage;

/**
 * @author Stepan Kopriva (stepan.kopriva at oracle.com)
 */
public class AuctionMessageDecoder implements Decoder.Text<AuctionMessage> {

    @Override
    public AuctionMessage decode(String s) {
        String[] tokens = s.split(":");

        return new AuctionMessage(tokens[0], tokens[1], tokens[2]);
    }

    @Override
    public boolean willDecode(String s) {
        return s.startsWith(AuctionMessage.BID_REQUEST) ||
                s.startsWith(AuctionMessage.AUCTION_LIST_REQUEST) ||
                s.startsWith(AuctionMessage.LOGIN_REQUEST) ||
                s.startsWith(AuctionMessage.LOGOUT_REQUEST);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // do nothing.
    }

    @Override
    public void destroy() {
        // do nothing.
    }
}
