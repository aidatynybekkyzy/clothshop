package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.OrderService;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
public class OrderController {
    private final OrderService orderService;
    private final ResponseErrorValidation responseErrorValidation;

    @GetMapping
    @ApiOperation("Getting list of orders")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}") //todo currentUser
    @ApiOperation("Getting the order by id")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @PostMapping("/{orderId}/items")
    @ApiOperation("Adding item to an order")
    public ResponseEntity<?> addItemToOrder(@PathVariable Long orderId, @RequestBody @Valid OrderItemDto orderItemDto, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = responseErrorValidation.mapValidationService(bindingResult);
        if (errorMap != null) return errorMap;
        OrderDto orderDto = orderService.addItemToOrder(orderId, orderItemDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    @ApiOperation("Cancel the order")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), " Order cancel success "));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Deleting the order by id")
    public ResponseEntity<ApiResponse> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), " Deleted successfully"));
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @ApiOperation("Getting the orderItem by orderId and itemId")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable Long orderId, @PathVariable @Valid Long itemId) {
        OrderItem orderItemDto = orderService.getItemOrder(orderId, itemId);
        return new ResponseEntity<>(orderItemDto, HttpStatus.OK);
    }

    @GetMapping("/{orderId}/items")
    @ApiOperation("Getting set of orderItems by orderId")
    public ResponseEntity<Set<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        Set<OrderItem> orderItemDtos = orderService.getAllOrderItems(orderId);
        return new ResponseEntity<>(orderItemDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @ApiOperation("Deleting orderItem by orderId and itemId")
    public ResponseEntity<ApiResponse> deleteOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        orderService.deleteOrderItem(orderId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.NO_CONTENT.value(), " Order Item deleted successfully"));
    }

    @PostMapping("/{id}/purchase")
    @ApiOperation("Purchasing the order by orderId")
    public ResponseEntity<ApiResponse> purchaseOrder(@PathVariable Long id) {
        orderService.purchaseOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), " Order paid successfully"));
    }
}
