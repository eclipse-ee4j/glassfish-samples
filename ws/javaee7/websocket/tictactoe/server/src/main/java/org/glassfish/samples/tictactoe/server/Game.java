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
package org.glassfish.samples.tictactoe.server;

import java.util.UUID;

/**
 *
 * @author johan
 */
public class Game {

	private String uid;
	
	private int[][] board = new int[3][3];
	private String player1;
	private String player2;

	public Game () {
		this.uid = UUID.randomUUID().toString();
	}
	
	/**
	 * @return the player1
	 */
	public String getPlayer1() {
		return player1;
	}

	/**
	 * @param player1 the player1 to set
	 */
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	/**
	 * @return the player2
	 */
	public String getPlayer2() {
		return player2;
	}

	/**
	 * @param player2 the player2 to set
	 */
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	
	
}
