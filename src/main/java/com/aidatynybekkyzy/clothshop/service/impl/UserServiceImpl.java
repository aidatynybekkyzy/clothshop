package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.ProductNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.UserEmailAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.UserNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.OrderMapper;
import com.aidatynybekkyzy.clothshop.mapper.UserMapper;
import com.aidatynybekkyzy.clothshop.model.*;
import com.aidatynybekkyzy.clothshop.repository.OrderRepository;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.repository.UserRepository;
import com.aidatynybekkyzy.clothshop.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, ProductRepository productRepository, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validateUniqueEmail(userDto.getEmail());
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        validateUniqueEmail(userDto.getEmail(), id);

        userMapper.updateEntity(userDto, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        return null;
    }

    @Override
    public OrderDto createOrderForCustomer(Long userId, OrderDto orderDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        //
        Order order = Order.builder()
                .shipDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PLACED.name())
                .complete(false)
                .orderItems(new ArrayList<>())
                .user(user).build();

        List<OrderItemDto> orderItemDtos = orderDto.getItems();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            Product product = productRepository.findById(orderItemDto.getId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + orderItemDto.getId()));

            OrderItem orderItem = OrderItem.builder()
                    .quantity(orderItemDto.getQuantity())
                    .price(orderItemDto.getPrice())
                    .product(product)
                    .order(order)
                    .build();

            order.getOrderItems().add(orderItem);
        }

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailAlreadyExistsException("User with this email already exists: " + email);
        }
    }

    private void validateUniqueEmail(String email, Long userId) {
        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new UserEmailAlreadyExistsException("User with this email already exists: " + email);
        }
    }

}