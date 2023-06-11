package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.model.response.ApiResponse;
import com.aidatynybekkyzy.clothshop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        return new ResponseEntity<>(orderService.getOrders(), HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        OrderDto order = orderService.getOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);

    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderDto> addItemToOrder(@PathVariable Long orderId, @RequestBody ProductDto productDto) {
        OrderDto orderDto = orderService.addItem(orderId, productDto);
        return new ResponseEntity<>(orderDto, HttpStatus.OK);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrderById(id);
        return  ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value()," Order cancel success "));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value(), " Deleted successfully"));
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<ProductDto> getOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        ProductDto orderItemDto = orderService.getItemOrder(orderId, itemId);
        return new ResponseEntity<>(orderItemDto, HttpStatus.OK);
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<Set<ProductDto>> getOrderItems(@PathVariable Long orderId) {
        Set<ProductDto> orderItemDtos = orderService.getAllOrderItems(orderId);
        return new ResponseEntity<>(orderItemDtos, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<ApiResponse> deleteOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        orderService.deleteItemOrder(orderId, itemId);
        return  ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.NO_CONTENT.value(), " Order Item deleted successfully"));
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<ApiResponse> purchaseOrder(@PathVariable Long id) {
        orderService.purchaseOrder(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(HttpStatus.OK.value()," Order paid successfully"));
    }
}
