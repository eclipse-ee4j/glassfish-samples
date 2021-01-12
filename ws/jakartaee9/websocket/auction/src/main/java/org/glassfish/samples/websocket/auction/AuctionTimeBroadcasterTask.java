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
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.websocket.Session;

import org.glassfish.samples.websocket.auction.message.AuctionMessage;

/**
 * @author Stepan Kopriva (stepan.kopriva at oracle.com)
 */
public class AuctionTimeBroadcasterTask extends TimerTask {

    private Auction owner;
    private int timeoutCounter;

    public AuctionTimeBroadcasterTask(Auction owner, int timeoutCounter) {
        this.owner = owner;
        this.timeoutCounter = timeoutCounter;
    }

    @Override
    public void run() {
        if (timeoutCounter < 0) {
            owner.switchStateToAuctionFinished();
        } else {
            if (!owner.getRemoteClients().isEmpty()) {
                AuctionMessage.AuctionTimeBroadcastMessage atbm = new AuctionMessage.AuctionTimeBroadcastMessage(owner.getId(), timeoutCounter);

                for (Session arc : owner.getRemoteClients()) {
                    try {
                        arc.getBasicRemote().sendText(atbm.toString());
                    } catch (IOException ex) {
                        Logger.getLogger(AuctionTimeBroadcasterTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        timeoutCounter--;
    }
}
