package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.service.UserService;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Api("Operations with users ")
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN')")
public class UserController {
    private final UserService userService;
    private final ResponseErrorValidation responseErrorValidation;

    public UserController(UserService userService, ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/createUser")
    @ApiOperation("Create new user ")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserDto userDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
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

    @GetMapping("/getUsers")
    @ApiOperation("Get all users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @ApiOperation("Update user")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {

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
    public ResponseEntity<?> createOrderForUser(@PathVariable Long userId, @RequestBody @Valid OrderDto orderDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        OrderDto createdOrderDto = userService.createOrderForCustomer(userId, orderDto);
        return ResponseEntity.ok(createdOrderDto);
    }

    @PostMapping("/{userId}/orders")
    @ApiOperation("Create an order for a customer ")
    public ResponseEntity<?> createOrderForUserByAdmin(@PathVariable Long userId, @RequestBody @Valid OrderDto orderDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        OrderDto createdOrderDto = userService.createOrderForCustomer(userId, orderDto);
        return ResponseEntity.ok(createdOrderDto);
    }

}
