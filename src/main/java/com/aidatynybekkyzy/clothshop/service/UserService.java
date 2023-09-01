package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.UserEmailAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserDetails loadUserByUsername(String username);
    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto createUser(UserDto userDto) throws UserEmailAlreadyExistsException;

    UserDto updateUser(Long id, UserDto userDto) throws UserEmailAlreadyExistsException;

    void deleteUser(Long id);

    List<OrderDto> getUserOrders(Long userId);

    OrderDto createOrderForCustomer(Long userId, OrderDto orderDto);

    Optional<User> findByUsername(String username);

}
