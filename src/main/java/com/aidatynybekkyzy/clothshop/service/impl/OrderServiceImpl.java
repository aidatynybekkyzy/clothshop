package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.ItemNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.OrderNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.OrderMapper;
import com.aidatynybekkyzy.clothshop.mapper.ProductMapper;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.OrderStatus;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.repository.OrderRepository;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;

    public OrderServiceImpl(
            OrderRepository orderRepository, OrderMapper orderMapper,
            ProductRepository productRepository, ProductMapper productMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Cacheable(value = "ordersCache")
    public List<OrderDto> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toDtoList(orders);
    }

    @Override
    @Cacheable(value = "ordersCache", key = "#id")
    public OrderDto getOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        return orderMapper.toDto(order);
    }

    @Override
    @CacheEvict(value = "ordersCache", allEntries = true)
    public OrderDto addItem(Long orderId, ProductDto productDto) {
        log.info("Item to add to order: " + productDto.toString());
        Order order = orderMapper.toEntity(getOrderById(orderId));

        if (productDto.getName() == null || productDto.getName().isEmpty()) {
            throw new InvalidArgumentException("Product name is required! ");
        }
        order.getItems().add(productMapper.toEntity(productDto));
        log.info("Item added to an order ");
        orderRepository.save(order);
        return orderMapper.toDto(order);

    }

    @Override
    @CacheEvict(value = "ordersCache", key = "#id")
    public void cancelOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        log.info("Canceling order SERVICE ");
        order.setStatus(OrderStatus.CANCELED.name());
        orderRepository.save(order);
    }

    @Override
    @CacheEvict(value = "ordersCache", key = "#id")
    public void deleteOrderById(Long id) {
        Order order = getOrderByIdIfExists(id);
        log.info("Deleting order SERVICE ");
        orderRepository.delete(order);
    }

    @Override
    @Cacheable(value = "orderItemsCache", key = "#orderId + '-' + #itemId")
    public ProductDto getItemOrder(Long orderId, Long itemId) {
        Order order = getOrderByIdIfExists(orderId);
        Product item = order.getItems()
                .stream()
                .filter(product -> product.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId));
        return orderMapper.toItemDto(item);
    }

    @Override
    @Cacheable(value = "orderItemsCache", key = "#orderId")
    public Set<ProductDto> getAllOrderItems(Long orderId) {
        log.info("Getting all order by id: " + orderId + " items");
        Order order = getOrderByIdIfExists(orderId);
        Set<Product> products = order.getItems();
        Set<ProductDto> productDtos = new HashSet<>();
        for (Product product : products) {
            ProductDto productDto = orderMapper.toItemDto(product);
            productDtos.add(productDto);
        }
        log.info("Set of all items:" + productDtos);
        return productDtos;
    }

    @Override
    @CacheEvict(value = "orderItemsCache", key = "#orderId + '-' + #itemId")
    public void deleteItemOrder(Long orderId, Long itemId) {
        log.info("Deleting order by id: " + orderId);

        Order order = getOrderByIdIfExists(orderId);
        Product item = order.getItems()
                .stream()
                .filter(product -> product.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + itemId));

        order.getItems().remove(item);
        orderRepository.save(order);

        log.info("Order deleted successfully");
    }

    @Override
    @CacheEvict(value = "ordersCache", key = "#id")
    public void purchaseOrder(Long id) {
        Order order = getOrderByIdIfExists(id);
        log.info("Purchasing order and changing status SERVICE ");
        order.setStatus(OrderStatus.PAID.name());
        order.setComplete(true);
        orderRepository.save(order);
    }

    private Order getOrderByIdIfExists(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
    }
}
