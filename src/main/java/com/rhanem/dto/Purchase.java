package com.rhanem.dto;


import com.rhanem.entity.Address;
import com.rhanem.entity.Customer;
import com.rhanem.entity.Order;
import com.rhanem.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class Purchase {

    private Customer customer;
    private Address shippingAddress;
    private Address billingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}
