package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.UserEmailAlreadyExistsException;

import java.util.List;

public interface UserService {
List<UserDto> getAllUsers();
UserDto getUserById(Long id);
UserDto createUser(UserDto userDto) throws UserEmailAlreadyExistsException;
UserDto updateUser(Long id, UserDto userDto) throws UserEmailAlreadyExistsException;
void deleteUser(Long id);

List<OrderDto> getUserOrders(Long userId);
OrderDto createOrderForCustomer(Long userId, OrderDto orderDto);

}
