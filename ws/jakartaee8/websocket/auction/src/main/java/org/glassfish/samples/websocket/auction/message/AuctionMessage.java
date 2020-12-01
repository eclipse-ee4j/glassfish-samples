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
package org.glassfish.samples.websocket.auction.message;

import org.glassfish.samples.websocket.auction.Auction;
import org.glassfish.samples.websocket.auction.AuctionItem;

/**
 * @author Stepan Kopriva (stepan.kopriva at oracle.com)
 */
public class AuctionMessage{

    public static final String LOGIN_REQUEST = "lreq";
    public static final String BID_REQUEST = "breq";
    public static final String LOGOUT_REQUEST = "dreq";
    public static final String AUCTION_LIST_REQUEST = "xreq";
    private static final String LOGIN_RESPONSE = "lres";
    private static final String PRICE_UPDATE_RESPONSE = "pres";
    private static final String AUCTION_TIME_RESPONSE = "ares";
    private static final String RESULT_RESPONSE = "rres";
    private static final String AUCTION_LIST_RESPONSE = "xres";

    /*
     * Message type
     */
    private final String type;

    /*
     * Message data
     */
    private final Object data;

    /*
     * ID used for communication purposes
     */
    private final String communicationId;

    /**
     * Create new message.
     *
     * @param type message type.
     * @param communicationId auction id.
     * @param data message data.
     */
    public AuctionMessage(String type, String communicationId, Object data) {
        this.type = type;
        this.communicationId = communicationId;
        this.data = data;
    }

    @Override
    public String toString() {
        return type + Auction.SEPARATOR + communicationId + Auction.SEPARATOR + data.toString();
    }

    /**
     * Get auction data.
     *
     * @return auction data.
     */
    public Object getData() {
        return data;
    }

    /**
     * ID of the auction this message is relevant to.
     *
     * @return auction id.
     */
    public String getCommunicationId() {
        return communicationId;
    }

    /**
     * Get message type.
     *
     * @return message type.
     */
    public String getType() {
        return type;
    }


    /**
     * @author Stepan Kopriva (stepan.kopriva at oracle.com)
     */
    public static class AuctionListResponseMessage extends AuctionMessage {

        public AuctionListResponseMessage(String communicationId, String data) {
            super(AUCTION_LIST_RESPONSE, communicationId, data);
        }
    }

    /**
     * @author Stepan Kopriva (stepan.kopriva at oracle.com)
     */
    public static class AuctionTimeBroadcastMessage extends AuctionMessage {

        public AuctionTimeBroadcastMessage(String communicationId, int time) {
            super(AUCTION_TIME_RESPONSE, communicationId, time);
        }
    }

    public static class LoginResponseMessage extends AuctionMessage {

        public LoginResponseMessage(String communicationId, AuctionItem item) {
            super(LOGIN_RESPONSE, communicationId, item);
        }
    }

    /**
     * @author Stepan Kopriva (stepan.kopriva at oracle.com)
     */
    public static class PriceUpdateResponseMessage extends AuctionMessage {

        public PriceUpdateResponseMessage(String communicationId, String price) {
            super(PRICE_UPDATE_RESPONSE, communicationId, price);
        }
    }

    /**
     * @author Stepan Kopriva (stepan.kopriva at oracle.com)
     */
    public static class ResultMessage extends AuctionMessage {

        public ResultMessage(String communicationId, String data) {
            super(RESULT_RESPONSE, communicationId, data);
        }
    }
}
