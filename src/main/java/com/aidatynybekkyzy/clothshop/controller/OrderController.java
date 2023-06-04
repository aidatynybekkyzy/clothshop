package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.exception.InvalidArgumentException;
import com.aidatynybekkyzy.clothshop.exception.ItemNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.OrderNotFoundException;
import com.aidatynybekkyzy.clothshop.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        try {
            OrderDto order = orderService.getOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (InvalidArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (OrderNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderDto> addItemToOrder(@PathVariable Long orderId, @RequestBody OrderItemDto orderItemDto){
        try {
            OrderDto orderDto = orderService.addOrderItem(orderId,orderItemDto);
            return new ResponseEntity<>(orderDto,HttpStatus.OK);
        } catch (OrderNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id){
        try {
            orderService.cancelOrderById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OrderNotFoundException e){
            return handleOrderNotFound();
        } catch (InvalidArgumentException e){
            return handleInvalidArgument();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable Long id){
        try {
            orderService.deleteOrderById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OrderNotFoundException e){
            return handleOrderNotFound();
        } catch (InvalidArgumentException e){
            return handleInvalidArgument();
        }
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderItemDto> getOrderItem(@PathVariable Long orderId,@PathVariable Long itemId){
        try {
            OrderItemDto orderItemDto = orderService.getItemOrder(orderId, itemId);
            return new ResponseEntity<>(orderItemDto, HttpStatus.OK);
        } catch (OrderNotFoundException | ItemNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemDto>> getOrderItems(@PathVariable Long orderId){
        try {
            List<OrderItemDto> orderItemDtos = orderService.getAllOrderItems(orderId);
            return new ResponseEntity<>(orderItemDtos, HttpStatus.OK);
        } catch (OrderNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (InvalidArgumentException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId, @PathVariable Long itemId){
        try {
            orderService.deleteItemOrder(orderId, itemId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OrderNotFoundException | ItemNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/purchase")
    public ResponseEntity<Void> purchaseOrder(@PathVariable Long id){
        try {
            orderService.purchaseOrder(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (OrderNotFoundException e){
            return handleOrderNotFound();
        } catch (InvalidArgumentException e){
            return handleInvalidArgument();
        }
    }

    private ResponseEntity<Void> handleOrderNotFound() {
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Void> handleInvalidArgument() {
        return ResponseEntity.badRequest().build();
    }
}
