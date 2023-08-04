package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface OrderService {
    @Transactional
    List<OrderDto> getOrders();
    @Transactional
    OrderDto getOrderById(Long id);
    @Transactional
    OrderDto addItem(Long id, OrderItemDto orderItemDto);
    @Transactional
    void cancelOrderById(Long id);
    @Transactional
    void deleteOrderById(Long id);
    @Transactional
    OrderItem getItemOrder(Long oid, Long iid);
    @Transactional
    Set<OrderItem> getAllOrderItems(Long oid);
    @Transactional
    void deleteOrderItem(Long oid, Long iid);
    @Transactional
    void purchaseOrder(Long id);

}
