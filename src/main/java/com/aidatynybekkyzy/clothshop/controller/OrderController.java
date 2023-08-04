package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @ApiOperation("Getting list of orders")
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    @ApiOperation("Getting the order by id")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @PostMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    @ApiOperation("Adding item to an order")
    public ResponseEntity<OrderDto> addItemToOrder(@PathVariable Long orderId, @RequestBody OrderItemDto orderItemDto) {
        OrderDto orderDto = orderService.addItem(orderId, orderItemDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    @ApiOperation("Cancel the order")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), " Order cancel success "));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    @ApiOperation("Deleting the order by id")
    public ResponseEntity<ApiResponse> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), " Deleted successfully"));
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    @ApiOperation("Getting the orderItem by orderId and itemId")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        OrderItem orderItemDto = orderService.getItemOrder(orderId, itemId);
        return new ResponseEntity<>(orderItemDto, HttpStatus.OK);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ADMIN')or isAuthenticated()")
    @ApiOperation("Getting set of orderItems by orderId")
    public ResponseEntity<Set<OrderItem>> getOrderItems(@PathVariable Long orderId) {
        Set<OrderItem> orderItemDtos = orderService.getAllOrderItems(orderId);
        return new ResponseEntity<>(orderItemDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    @ApiOperation("Deleting orderItem by orderId and itemId")
    public ResponseEntity<ApiResponse> deleteOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        orderService.deleteOrderItem(orderId, itemId);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.NO_CONTENT.value(), " Order Item deleted successfully"));
    }

    @PostMapping("/{id}/purchase")
    @PreAuthorize("hasRole('ADMIN') or isAuthenticated()")
    @ApiOperation("Purchasing the order by orderId")
    public ResponseEntity<ApiResponse> purchaseOrder(@PathVariable Long id) {
        orderService.purchaseOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), " Order paid successfully"));
    }
}
