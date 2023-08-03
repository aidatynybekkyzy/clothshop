package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.model.OrderItem;

import java.util.List;
import java.util.Set;

public interface OrderService {
    List<OrderDto> getOrders();

    OrderDto getOrderById(Long id);

    OrderDto addItem(Long id, OrderItemDto orderItemDto);

    void cancelOrderById(Long id);

    void deleteOrderById(Long id);

    OrderItem getItemOrder(Long oid, Long iid);

    Set<OrderItem> getAllOrderItems(Long oid);

    void deleteOrderItem(Long oid, Long iid);

    void purchaseOrder(Long id);

}
