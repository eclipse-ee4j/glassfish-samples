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

package enterprise.criteriaQuery.ejb;

import enterprise.criteriaQuery.entity.*;

import java.io.IOException;
import javax.ejb.Stateless;
import javax.persistence.*;
import javax.servlet.ServletOutputStream;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.MapJoin;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;

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
        CriteriaBuilder criteriaBuilder = em.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<Order> cquery = criteriaBuilder.createQuery(Order.class);
        Root<Order> order = cquery.from(Order.class);
        MapJoin<Order, Item, LineItem> lineItemsMap = order.joinMap("lineItems");
        cquery.
                select(order).
                where(
                    criteriaBuilder.equal(
                            lineItemsMap.key().get("name"),criteriaBuilder.parameter(String.class, "name")
                            )
                    );
        TypedQuery<Order> tq = em.createQuery(cquery);
        tq.setParameter("name", itemName);

        List<Order> orders = tq.getResultList();
        for (Order o : orders) {
            outputStream.println("Retrieved Order id is: " + o.getId());
        }
    }

    private void queryDataForOrder(int orderId, ServletOutputStream outputStream) throws IOException {
        outputStream.println("\n\nQuerying for Map entries for Order Id : " + orderId);
        CriteriaBuilder criteriaBuilder = em.getEntityManagerFactory().getCriteriaBuilder();

        CriteriaQuery<Map.Entry> cquery = criteriaBuilder.createQuery(Map.Entry.class);

        Root<Order> order = cquery.from(Order.class);
        MapJoin<Order, Item, LineItem> lineItemsMap = order.joinMap("lineItems");
        cquery.
                select(lineItemsMap.entry()).
                where(
                    criteriaBuilder.equal(
                            order.get("id"),criteriaBuilder.parameter(Integer.class, "id")
                            )
                    );
        TypedQuery<Map.Entry> tq = em.createQuery(cquery);
        tq.setParameter("id", orderId);
        List<Map.Entry> entrySet = tq.getResultList();

        for (Map.Entry<Item, LineItem> itemLineItemEntry : entrySet) {
            Item item = itemLineItemEntry.getKey();
            LineItem lineItem  = itemLineItemEntry.getValue();
            outputStream.println("\tLineIten: " + lineItem.getLineItemNumber() + "\tItem: " + item.getName() + "\tQty Ordered: " + lineItem.getQuantity());
        }
    }

}
