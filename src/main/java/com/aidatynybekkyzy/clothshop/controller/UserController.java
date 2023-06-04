package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.UserNotFoundException;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        try {
            UserDto createdUser = userService.createUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {

        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        } catch (UserNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<?> getUserOrders(@PathVariable Long userId) {
        try {
            List<OrderDto> orders = userService.getUserOrders(userId);
            return ResponseEntity.ok(orders);
        } catch (UserNotFoundException e) {
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e) {
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/{userId}/orders")
    public ResponseEntity<?> createOrderForUser(@PathVariable Long userId, @RequestBody OrderDto orderDto){
        try {
            OrderDto orderDto1 = userService.createOrderForCustomer(userId, orderDto);
            return ResponseEntity.ok(orderDto1);
        } catch (UserNotFoundException e){
            return handleException(e, HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e){
            return handleException(e, HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<ApiResponse> handleException(Exception exception, HttpStatus status) {
        ApiResponse errorResponse = new ApiResponse(status.value(), "Error", exception.getMessage());
        return ResponseEntity.status(status).body(errorResponse);

    }
}
