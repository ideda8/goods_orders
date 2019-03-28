package com.da.cart;


import com.da.order.OrderItem;

import java.util.List;

/**
 * 购物车对象
 */
public class Cart {
    private Long id;
    private List<OrderItem> orderItemsList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItemsList() {
        return orderItemsList;
    }

    public void setOrderItemsList(List<OrderItem> orderItemsList) {
        this.orderItemsList = orderItemsList;
    }
}
