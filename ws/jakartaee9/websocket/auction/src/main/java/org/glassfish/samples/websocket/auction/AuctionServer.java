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

package org.glassfish.samples.websocket.auction;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import org.glassfish.samples.websocket.auction.decoders.AuctionMessageDecoder;
import org.glassfish.samples.websocket.auction.encoders.AuctionMessageEncoder;
import org.glassfish.samples.websocket.auction.message.AuctionMessage;

/**
 * Runs multiple auctions.
 * @author Stepan Kopriva (stepan.kopriva at oracle.com)
 */
@ServerEndpoint(value = "/auction",
        decoders = {
                AuctionMessageDecoder.class,
        },
        encoders = {
                AuctionMessageEncoder.class
        }
)
public class AuctionServer {

    /*
     * Set of auctions (finished, running, to be started auctions).
     */
    private static final Set<Auction> auctions = Collections.unmodifiableSet(new HashSet<Auction>() {{
        add(new Auction(new AuctionItem("Swatch", "Nice Swatch watches, hand made", 100, 20)));
        add(new Auction(new AuctionItem("Rolex", "Nice Rolex watches, hand made", 200, 20)));
        add(new Auction(new AuctionItem("Omega", "Nice Omega watches, hand made", 300, 20)));
    }});

    @OnClose
    public void handleClosedConnection(Session session) {
        for (Auction auction : auctions) {
            auction.removeArc(session);
        }
    }

    @OnMessage
    public void handleMessage(AuctionMessage message, Session session){
        String communicationId;

        switch (message.getType()){
            case AuctionMessage.LOGOUT_REQUEST:
                handleClosedConnection(session);
                break;
            case AuctionMessage.AUCTION_LIST_REQUEST:
                StringBuilder sb = new StringBuilder("-");

                for (Auction auction : auctions) {
                    sb.append(auction.getId()).append("-").append(auction.getItem().getName()).append("-");
                }

                try {
                    session.getBasicRemote().sendObject((new AuctionMessage.AuctionListResponseMessage("0", sb.toString())));
                } catch (IOException | EncodeException e) {
                    Logger.getLogger(AuctionServer.class.getName()).log(Level.SEVERE, null, e);
                }
                break;
            case AuctionMessage.LOGIN_REQUEST:
                communicationId = message.getCommunicationId();
                for (Auction auction : auctions) {
                    if (communicationId.equals(auction.getId())) {
                        auction.handleLoginRequest(message, session);
                    }
                }
                break;
            case AuctionMessage.BID_REQUEST:
                communicationId = message.getCommunicationId();
                for (Auction auction : auctions) {
                    if (communicationId.equals(auction.getId())) {
                        auction.handleBidRequest(message, session);
                        break;
                    }
                }
                break;
        }

    }
}
