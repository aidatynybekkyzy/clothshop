package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.UserEmailAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.exception.UserNotFoundException;
import com.aidatynybekkyzy.clothshop.mapper.OrderMapper;
import com.aidatynybekkyzy.clothshop.mapper.UserMapper;
import com.aidatynybekkyzy.clothshop.model.Order;
import com.aidatynybekkyzy.clothshop.model.OrderStatus;
import com.aidatynybekkyzy.clothshop.model.Product;
import com.aidatynybekkyzy.clothshop.model.User;
import com.aidatynybekkyzy.clothshop.repository.OrderRepository;
import com.aidatynybekkyzy.clothshop.repository.ProductRepository;
import com.aidatynybekkyzy.clothshop.repository.UserRepository;
import com.aidatynybekkyzy.clothshop.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        log.info("SERVICE  Get user by id");
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return userMapper.toDto(user);
    }

    @Override
    public UserDto createUser( @Valid UserDto userDto) throws UserEmailAlreadyExistsException {
        validateUniqueEmail(userDto.getEmail());
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) throws UserEmailAlreadyExistsException {
        User existingUser = userMapper.toEntity(getUserById(id));

        validateUniqueEmail(userDto.getEmail(), id);

        userMapper.updateEntity(userDto, existingUser);
        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userMapper.toEntity(getUserById(id));
        userRepository.delete(user);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        User user = userMapper.toEntity(getUserById(userId));
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto createOrderForCustomer(Long userId, OrderDto orderDto) {
        User user = userMapper.toEntity(getUserById(userId));

        Order order = Order.builder()
                .shipDate(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .status(OrderStatus.PLACED.name())
                .complete(false)
                .items(new HashSet<>())
                .user(user)
                .build();

        List<ProductDto> orderItems = orderDto.getItems();

        for (ProductDto orderItemDto : orderItems) {
            Product orderItem = Product.builder()
                    .name(orderItemDto.getName())
                    .price(orderItemDto.getPrice())
                    .quantity(orderItemDto.getQuantity())
                    .categoryId(orderItemDto.getCategoryId())
                    .vendorId(orderItemDto.getVendorId())
                    .build();

            order.getItems().add(orderItem);
        }
        user.getOrders().add(order); // Добавляем созданный заказ к списку заказов пользователя

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    private void validateUniqueEmail(String email) throws UserEmailAlreadyExistsException {
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailAlreadyExistsException("User with this email already exists: " + email);
        }
    }

    private void validateUniqueEmail(String email, Long userId) throws UserEmailAlreadyExistsException {
        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new UserEmailAlreadyExistsException("User with this email already exists: " + email);
        }
    }

}
