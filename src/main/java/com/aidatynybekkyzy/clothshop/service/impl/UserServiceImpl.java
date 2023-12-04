package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.enums.OrderStatus;
import com.aidatynybekkyzy.clothshop.exception.EntityAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.EntityNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.OrderMapper;
import com.aidatynybekkyzy.clothshop.mapper.UserMapper;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import com.aidatynybekkyzy.clothshop.model.User;
import com.aidatynybekkyzy.clothshop.repository.OrderRepository;
import com.aidatynybekkyzy.clothshop.repository.UserRepository;
import com.aidatynybekkyzy.clothshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;


    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper,
                           OrderRepository orderRepository,
                           OrderMapper orderMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    @Transactional
    public UserDto getUserById(Long id) {
        log.info("SERVICE  Get user by id");
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto createUser(@Valid UserDto userDto) {
        validateUniqueEmail(userDto.getEmail());
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userMapper.toEntity(getUserById(id));

        validateUniqueEmail(userDto.getEmail(), id);

        userMapper.updateEntity(userDto, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userMapper.toEntity(getUserById(id));
        userRepository.delete(user);
    }

    @Override
    @Transactional
    public List<OrderDto> getUserOrders(Long userId) {
        User user = userMapper.toEntity(getUserById(userId));
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderDto createOrderForCustomer(Long userId, @NotNull OrderDto orderDto) {
        User user = userMapper.toEntity(getUserById(userId));
        log.info("creating new order");
        Order order = Order.builder()
                .shipDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PLACED.name())
                .complete(false)
                .orderItems(new HashSet<>())
                .user(user)
                .build();
        order.setId(orderDto.getId());

        Set<OrderItemDto> items = orderDto.getItems();
        for (OrderItemDto orderItem : items) {
            OrderItem item = OrderItem.builder()
                    .quantity(orderItem.getQuantity())
                    .sellingPrice(orderItem.getSellingPrice())
                    .productId(orderItem.getProductId())
                    .build();
            item.setId(orderItem.getId());
            order.add(item);
        }
        user.add(order);
        log.info("OrderItem added to order");
        Order savedOrder = orderRepository.save(order);

        return orderMapper.toDto(savedOrder);
    }


    private void validateUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException("User with this email already exists: " + email);
        }
    }

    private void validateUniqueEmail(String email, Long userId) {
        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new EntityAlreadyExistsException("User with this email already exists: " + email);
        }
    }

}
