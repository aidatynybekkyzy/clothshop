package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.exception.ItemNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.OrderNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.OrderMapper;
import com.aidatynybekkyzy.clothshop.mapper.ProductMapper;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import com.aidatynybekkyzy.clothshop.model.OrderStatus;
import com.aidatynybekkyzy.clothshop.repository.OrderRepository;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.repository.UserRepository;
import com.aidatynybekkyzy.clothshop.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(
                            OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDtoList(orders);
    }

    @Override
    public OrderDto getOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto addOrderItem(Long orderId, OrderItemDto orderItemDto) {
        Order order = orderMapper.toEntity(getOrderById(orderId));
        order.getOrderItems().add(orderMapper.toOrderItemEntity(orderItemDto));
        orderRepository.save(order);
        return orderMapper.toDto(order);

    }

    @Override
    public void cancelOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        order.setStatus(OrderStatus.CANCELED.name());
    }

    @Override
    public void deleteOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        orderRepository.delete(order);
    }

    @Override
    public OrderItemDto getItemOrder(Long orderId, Long itemId) {
        Order order = getOrderByIdIfExists(orderId);
        OrderItem item = order.getOrderItems()
                .stream()
                .filter(product -> product.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId));
        return orderMapper.toOrderItemDto(item);
    }

    @Override
    public List<OrderItemDto> getAllOrderItems(Long orderId) {
        Order order = getOrderByIdIfExists(orderId);
        return orderMapper.toOrderItemDtoList(order.getOrderItems());
    }

    @Override
    public void deleteItemOrder(Long orderId, Long itemId) {
        Order order = getOrderByIdIfExists(orderId);
        OrderItem item = order.getOrderItems()
                .stream()
                .filter(product -> product.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId));
        order.getOrderItems().remove(item);
        orderRepository.save(order);
    }

    @Override
    public void purchaseOrder(Long id) {
        Order order = getOrderByIdIfExists(id);
        order.setStatus(OrderStatus.PAID.name());
    }

    private Order getOrderByIdIfExists(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }
}
