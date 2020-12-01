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

package org.glassfish.samples.tictactoe.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import jakarta.websocket.Endpoint;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.MessageHandler;
import jakarta.websocket.Session;

/**
 * @author Johan
 */
public class LocalEndpoint extends Endpoint implements MessageHandler.Whole<String> {

    public static TicTacToeClient tictactoe;
    private Session session;

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        System.out.println("Endpoint opened, session = " + session + ", config = " + config);
        this.session = session;
        session.addMessageHandler(this);
        tictactoe.setEndpoint(this);
    }

    @Override
    public void onMessage(final String message) {
        Platform.runLater(new Runnable() {
            public void run() {

                System.out.println("GOT MESSAGE: " + message);
                if ("p1".equals(message)) {
                    tictactoe.setInfo("Waiting for a second player to join...");
                }
                if ("p2".equals(message)) {
                    tictactoe.setInfo("You play 'O'");
                    tictactoe.setSymbol(1);
                    tictactoe.setOtherSymbol(2);
                    tictactoe.myTurn(true);
                }
                if ("p3".equals(message)) {
                    tictactoe.setInfo("You play 'X'");
                    tictactoe.setSymbol(2);
                    tictactoe.setOtherSymbol(1);
                }
                if (message.startsWith("o")) {
                    int c = Integer.parseInt(message.substring(1));
                    tictactoe.doMove(c);
                }
                if (message.startsWith("x")) {
                    int c = Integer.parseInt(message.substring(1));
                    tictactoe.doMove(c);
                }

            }
        });

    }

    void myMove(int coords, int symbol) {
        String move = ((symbol == 1) ? "o" : "x") + coords;
        System.out.println("send this move: " + move);
        try {
            session.getBasicRemote().sendText(move);
        } catch (IOException ex) {
            Logger.getLogger(LocalEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void done() throws IOException {
        System.out.println("closing session");
        session.close();
        System.out.println("closed session");
    }
}
