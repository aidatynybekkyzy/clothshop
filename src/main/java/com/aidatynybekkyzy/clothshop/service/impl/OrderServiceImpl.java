package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.exception.EntityNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.OrderItemMapper;
import com.aidatynybekkyzy.clothshop.mapper.OrderMapper;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import com.aidatynybekkyzy.clothshop.enums.OrderStatus;
import com.aidatynybekkyzy.clothshop.repository.OrderItemRepository;
import com.aidatynybekkyzy.clothshop.repository.OrderRepository;
import com.aidatynybekkyzy.clothshop.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;


    public OrderServiceImpl(
            OrderRepository orderRepository, OrderMapper orderMapper,
            OrderItemMapper orderItemMapper, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @CacheEvict(value = "ordersCache", allEntries = true)
    @Transactional
    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDtoList(orders);
    }

    @Override
    @Cacheable(value = "ordersCache", key = "#id")
    @Transactional
    public OrderDto getOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto addItemToOrder(Long orderId, OrderItemDto orderItemDto) {

        log.info("Item to add to order: " + orderItemDto.toString());

        Order order = getOrderByIdIfExists(orderId);

        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);
        orderItem.setOrder(order);

        order.getOrderItems().add(orderItem);

        Order savedOrder = orderRepository.save(order);
        log.info("Item added to an order ");
        return orderMapper.toDto(savedOrder);

    }


    @Override
    @CacheEvict(value = "ordersCache", key = "#id")
    @Transactional
    public void cancelOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        log.info("Canceling order SERVICE ");
        order.setStatus(OrderStatus.CANCELED.name());
        orderRepository.save(order);
    }

    @Override
    @CacheEvict(value = "ordersCache", key = "#id")
    @Transactional
    public void deleteOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        log.info("Deleting order with id -- " +  id);
        orderRepository.delete(order);
    }

    @Override
    @Cacheable(value = "orderItemsCache", key = "#orderId + '-' + #itemId")
    @Transactional
    public OrderItem getItemOrder(Long orderId, Long itemId) {
        Order order = getOrderByIdIfExists(orderId);
        return order.getOrderItems()
                .stream()
                .filter(product -> product.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));
    }

    @Override
    @CacheEvict(value = "orderItemsCache", key = "#orderId")
    @Transactional
    public Set<OrderItem> getAllOrderItems(Long orderId) {
        log.info("Getting all order by id: " + orderId + " items");
        Order order = getOrderByIdIfExists(orderId);
        return order.getOrderItems();
    }

    @Override
    @CacheEvict(value = "orderItemsCache", key = "#orderId + '-' + #itemId")
    @Transactional
    public void deleteOrderItem(Long orderId, Long itemId) {
        Order order = getOrderByIdIfExists(orderId);
        OrderItem orderItem = order.getOrderItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("Order item with id: '%s' not found in order with id: '%s'", itemId, orderId)));
        order.remove(orderItem.getId());
        orderItemRepository.delete(orderItem);
        orderRepository.save(order);
    }

    @Override
    @CacheEvict(value = "ordersCache", key = "#id")
    @Transactional
    public void purchaseOrder(Long id) {
        Order order = getOrderByIdIfExists(id);
        log.info("Purchasing order and changing status SERVICE ");
        order.setStatus(OrderStatus.PAID.name());
        order.setComplete(true);
        orderRepository.save(order);
    }

    private Order getOrderByIdIfExists(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }
}
