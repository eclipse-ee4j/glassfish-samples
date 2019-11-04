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

package enterprise.advancedmapping.ejb;

import enterprise.advancedmapping.entity.*;

import java.io.IOException;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;

@Stateless
/**
 * Contains methods to create and query data
 */
public class StatelessSessionBean {

    @PersistenceContext
    private EntityManager em;

    public void createData(ServletOutputStream outputStream) {
        final int NO_OF_ITEMS = 10;

        //Create Items
        for(int i = 1 ; i <= NO_OF_ITEMS ; i++) {
            Item item = new Item(i, "ItemName" + i, i * 10);
            em.persist(item);
        }
        final int NO_OF_ORDERS = 1;
        for(int i = 1 ; i <= NO_OF_ORDERS ; i++) {
            createOrder(i);
        }
    }

    private void createOrder(int orderNumber) {
        Order o  = new Order("customer" + orderNumber);
        for(int lineItemNumber = 1 ; lineItemNumber <= 3 ; lineItemNumber++) {
            LineItem li = new LineItem(lineItemNumber, o,  lineItemNumber * orderNumber);
            o.addLineItem(em.find(Item.class, lineItemNumber), li);
        }
        em.persist(o);
    }

    public void queryData(ServletOutputStream outputStream) throws IOException {
        queryForOrderContainingItem("ItemName1", outputStream);
        queryDataForOrder(1, outputStream);

    }

    private void queryForOrderContainingItem(String itemName, ServletOutputStream outputStream) throws IOException {
        outputStream.println("\n\nQuerying for Order containing item : " + itemName);
        String query = "select o from Order o join o.lineItems li where KEY(li).name = ?1";
        Query q = em.createQuery(query);
        q.setParameter(1, itemName);
        outputStream.println("Executing query..." + query);
        List<Order> orders = q.getResultList();
        for (Order order : orders) {
            outputStream.println("Retrieved Order id is: " + order.getId());
        }
    }

    private void queryDataForOrder(int orderId, ServletOutputStream outputStream) throws IOException {
        outputStream.println("\n\nQuerying for Map entries for Order Id : " + orderId);
        String query = "select ENTRY(li) from Order o join o.lineItems li where o.id = ?1";
        Query q = em.createQuery(query);
        q.setParameter(1, orderId);
        outputStream.println("Executing query..." + query);
        List<Map.Entry<Item, LineItem>> entrySet = q.getResultList();
        for (Map.Entry<Item, LineItem> itemLineItemEntry : entrySet) {
            Item item = itemLineItemEntry.getKey();
            LineItem lineItem  = itemLineItemEntry.getValue();
            outputStream.println("\tLineIten: " + lineItem.getLineItemNumber() + "\tItem: " + item.getName() + "\tQty Ordered: " + lineItem.getQuantity());
        }
    }

}
