package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;

import java.util.List;

public interface OrderService {
List<OrderDto> getOrders();
OrderDto getOrderById(Long id);
OrderDto addOrderItem(Long id, OrderItemDto orderItemDto);
void cancelOrderById(Long id);
void deleteOrderById(Long id);
OrderItemDto getItemOrder(Long oid, Long iid);
List<OrderItemDto> getAllOrderItems(Long oid);
void deleteItemOrder(Long oid, Long iid);
void purchaseOrder(Long id);

}
