package com.aidatynybekkyzy.clothshop.service;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;

import java.util.List;

public interface UserService {
List<UserDto> getAllUsers();
UserDto getUserById(Long id);
UserDto createUser(UserDto userDto);
UserDto updateUser(Long id, UserDto userDto);
void deleteUser(Long id);

List<OrderDto> getUserOrders(Long userId);
OrderDto createOrderForCustomer(Long userId, OrderDto orderDto);

}
