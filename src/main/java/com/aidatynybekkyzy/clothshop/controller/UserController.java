package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.AccessDeniedException;
import com.aidatynybekkyzy.clothshop.exception.UserEmailAlreadyExistsException;
import com.aidatynybekkyzy.clothshop.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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

    @PostMapping("/admin")
    @ApiOperation("Create new user ")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (AccessDeniedException | UserEmailAlreadyExistsException e) {
            String errorMessage = "Access denied: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMessage);
        }
    }


    @GetMapping("/{id}")
    @ApiOperation("Get user by id ")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("CONTROLLER Get user by id ");
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin/getUsers")
    @ApiOperation("Get all users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.PATCH})
    @ApiOperation("Update user")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) throws UserEmailAlreadyExistsException {

        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/admin/{id}")
    @ApiOperation("Delete user")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/orders")
    @ApiOperation("Get user's orders ")
    @PreAuthorize("hasRole('ADMIN')or isAuthenticated()")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable Long userId) {
        List<OrderDto> orders = userService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{userId}/orders")
    @ApiOperation("Create an order for a customer ")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public ResponseEntity<OrderDto> createOrderForUser(@PathVariable Long userId, @RequestBody OrderDto orderDto) {
        OrderDto createdOrderDto = userService.createOrderForCustomer(userId, orderDto);
        return ResponseEntity.ok(createdOrderDto);
    }
    @PostMapping("/admin/{userId}/orders")
    @ApiOperation("Create an order for a customer ")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    public ResponseEntity<OrderDto> createOrderForUserByAdmin(@PathVariable Long userId, @RequestBody OrderDto orderDto) {
        OrderDto createdOrderDto = userService.createOrderForCustomer(userId, orderDto);
        return ResponseEntity.ok(createdOrderDto);
    }

}
