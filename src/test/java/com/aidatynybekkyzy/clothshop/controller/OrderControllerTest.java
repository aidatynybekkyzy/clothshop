package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.JsonUtils;
import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.exception.exceptionhandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import com.aidatynybekkyzy.clothshop.enums.OrderStatus;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import com.aidatynybekkyzy.clothshop.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderServiceImpl orderService;
    @Mock
    ResponseErrorValidation responseErrorValidation;
    MockMvc mockMvc;
    private final static long ID = 1L;

    OrderItemDto orderItem1 = OrderItemDto.builder()
            .id(1L)
            .productId(1L)
            .quantity(2)
            .sellingPrice(new BigDecimal(25))
            .build();
    OrderItemDto orderItem2 = OrderItemDto.builder()
            .id(2L)
            .productId(2L)
            .quantity(2)
            .sellingPrice(new BigDecimal(25))
            .build();
    OrderItem orderItem11 = OrderItem.builder()
            .productId(orderItem1.getProductId())
            .quantity(orderItem1.getQuantity())
            .sellingPrice(orderItem1.getSellingPrice())
            .build();

    OrderItem orderItem22 = OrderItem.builder()
            .productId(orderItem2.getProductId())
            .quantity(orderItem2.getQuantity())
            .sellingPrice(orderItem2.getSellingPrice())
            .build();
    Set<OrderItem> orderItems = Set.of(orderItem11, orderItem22);
    final OrderDto order1 = OrderDto.builder()
            .id(ID)
            .items(Set.of(orderItem1, orderItem2))
            .userId(1L)
            .build();
    final OrderDto order2 = OrderDto.builder()
            .id(2L)
            .items(Set.of(orderItem1, orderItem2))
            .userId(1L)
            .build();
    List<OrderDto> orders = List.of(order1, order2);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController, responseErrorValidation)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAllOrders() throws Exception {
        when(orderService.getOrders()).thenReturn(orders);

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].items[0].quantity", is(2)))
                .andExpect(jsonPath("$[0].items[0].productId", is(1)))
                .andExpect(jsonPath("$[0].items[0].sellingPrice", is(25)))
                .andExpect(jsonPath("$[1].items[0].quantity", is(2)))
                .andExpect(jsonPath("$[1].items[0].productId", is(2)))
                .andExpect(jsonPath("$[1].items[0].sellingPrice", is(25)));

        verify(orderService, times(1)).getOrders();
    }

    @Test
    void getOrderById() throws Exception {
        when(orderService.getOrderById(ID)).thenReturn(order1);

        mockMvc.perform(get("/orders/{orderId}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity", is(2)))
                .andExpect(jsonPath("$.items[0].productId", is(1)))
                .andExpect(jsonPath("$.items[0].sellingPrice", is(25)))
                .andDo(print());
    }

    @Test
    void addItemToOrder() throws Exception {
        Long orderId = 1L;
        OrderItemDto orderItem = OrderItemDto.builder()
                .id(1L)
                .productId(1L)
                .quantity(2)
                .sellingPrice(new BigDecimal(25)).build();
        when(orderService.addItemToOrder(orderId, orderItem)).thenReturn(order1);
        mockMvc.perform(post("/orders/{orderId}/items", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(orderItem)))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.asJsonString(order1)));
        verify(orderService, times(1)).addItemToOrder(orderId, orderItem1);
    }

    @Test
    void cancelOrder() throws Exception {
        Long orderId = 1L;
        doAnswer(invocation -> {
            order1.setStatus(OrderStatus.CANCELED.name());
            return null;
        }).when(orderService).cancelOrderById(orderId);
        mockMvc.perform(post("/orders/{id}/cancel", orderId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"message\":\" Order cancel success \"}"));

        verify(orderService).cancelOrderById(orderId);

    }

    @Test
    void deleteOrderById() throws Exception {
        Long orderId = 1L;
        mockMvc.perform(delete("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"message\":\" Deleted successfully\"}"));

        verify(orderService, times(1)).deleteOrderById(orderId);
    }

    @Test
    void getOrderItem() throws Exception {
        Long orderId = 1L;
        Long itemId = 1L;
        OrderItem orderItem = OrderItem.builder()
                .productId(orderItem1.getProductId())
                .quantity(orderItem1.getQuantity())
                .sellingPrice(orderItem1.getSellingPrice())
                .build();
        orderItem.setId(orderItem1.getId());
        when(orderService.getItemOrder(orderId, itemId)).thenReturn(orderItem);
        mockMvc.perform(get("/orders/{orderId}/items/{itemId}", orderId, itemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId", is(1)))
                .andExpect(jsonPath("$.quantity", is(2)))
                .andExpect(jsonPath("$.sellingPrice").value(25))
                .andDo(print());
    }

    @Test
    void getOrderItems() throws Exception {
        Long orderId = 1L;
        when(orderService.getAllOrderItems(orderId)).thenReturn(orderItems);

        mockMvc.perform(get("/orders/{orderId}/items", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(orderItems.size()))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].sellingPrice").value(25))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].quantity").value(2))
                .andExpect(jsonPath("$[1].sellingPrice").value(25))
                .andDo(print());
    }

    @Test
    void deleteOrderItem() throws Exception {
        Long orderId = 1L;
        Long itemId = 1L;
        mockMvc.perform(delete("/orders/{orderId}/items/{itemId}", orderId, itemId))
                .andExpect(status().isOk())
                .andDo(print());
        verify(orderService, times(1)).deleteOrderItem(orderId, itemId);
    }

    @Test
    void purchaseOrder() throws Exception {
        Long orderId = 1L;
        doAnswer(invocation -> {
            order1.setStatus(OrderStatus.PAID.name());
            order1.setComplete(true);
            return null;
        }).when(orderService).purchaseOrder(orderId);
        mockMvc.perform(post("/orders/{id}/purchase", orderId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"statusCode\":200,\"message\":\" Order paid successfully\"}"));

        verify(orderService).purchaseOrder(orderId);
        assertEquals(OrderStatus.PAID.name(), order1.getStatus());
        assertTrue(order1.getComplete());

    }
}