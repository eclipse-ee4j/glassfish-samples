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
package org.glassfish.samples.websocket.auction;

/**
 * @author Stepan Kopriva (stepan.kopriva at oracle.com)
 */
public class AuctionItem {

    /*
     * Name of the item.
     */
    private final String name;

    /*
     * Description of the item.
     */
    private final String description;

    /*
     * Current price of the item.
     */
    private double price;

    /*
     * Timeout which is applied for one bid.
     */
    private final int bidTimeoutS;

    public AuctionItem(String name, String description, double price, int bidTimeoutS) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.bidTimeoutS = bidTimeoutS;
    }

    @Override
    public String toString() {
        return name + Auction.SEPARATOR + description + Auction.SEPARATOR + price + Auction.SEPARATOR + "0" + Auction.SEPARATOR + bidTimeoutS + " seconds";
    }

    public int getBidTimeoutS() {
        return bidTimeoutS;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public String getName() {
        return name;
    }
}
