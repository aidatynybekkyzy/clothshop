package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;

import java.util.List;
import java.util.Set;

public interface OrderService {
    List<OrderDto> getOrders();

    OrderDto getOrderById(Long id);

    OrderDto addItem(Long id, ProductDto productDto);

    void cancelOrderById(Long id);

    void deleteOrderById(Long id);

    ProductDto getItemOrder(Long oid, Long iid);

    Set<ProductDto> getAllOrderItems(Long oid);

    void deleteItemOrder(Long oid, Long iid);

    void purchaseOrder(Long id);

}
