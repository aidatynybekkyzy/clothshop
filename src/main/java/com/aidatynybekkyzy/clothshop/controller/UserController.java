package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.UserEmailAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Api("Operations with users ")
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation("Create new user ")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) throws UserEmailAlreadyExistsException {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get user by id ")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("CONTROLLER Get user by id ");
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @ApiOperation("Get all users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update user")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws UserEmailAlreadyExistsException {

        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/orders")
    @ApiOperation("Get user's orders ")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable Long userId) {
        List<OrderDto> orders = userService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{userId}/orders")
    @ApiOperation("Create an order for a customer ")
    public ResponseEntity<OrderDto> createOrderForUser(@PathVariable Long userId, @RequestBody OrderDto orderDto) {
        OrderDto orderDto1 = userService.createOrderForCustomer(userId, orderDto);
        return ResponseEntity.ok(orderDto1);
    }
}
