package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.JsonUtils;
import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.exceptionHandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.mapper.OrderItemMapper;
import com.aidatynybekkyzy.clothshop.model.OrderItem;
import com.aidatynybekkyzy.clothshop.model.OrderStatus;
import com.aidatynybekkyzy.clothshop.repository.OrderItemRepository;
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
    @InjectMocks
    private OrderItemMapper orderItemMapper;
    MockMvc mockMvc;
    private final static long ID = 1L;

    final ProductDto product1 = ProductDto.builder()
            .id(ID)
            .name("Adidas")
            .price(new BigDecimal(1000))
            .quantity(5)
            .categoryId(1L)
            .vendorId(1L)
            .build();
    final ProductDto product2 = ProductDto.builder()
            .id(2L)
            .name("Nike")
            .price(new BigDecimal(500))
            .quantity(3)
            .categoryId(1L)
            .vendorId(1L)
            .build();
    Set<ProductDto> productDtos = Set.of(product1, product2);
    OrderItemDto orderItem1 = OrderItemDto.builder()
            .id(1L)
            .productId(1L)
            .quantity(2)
            .sellingPrice(new BigDecimal("25.00"))
            .build();
    OrderItemDto orderItem2 = OrderItemDto.builder()
            .id(2L)
            .productId(2L)
            .quantity(2)
            .sellingPrice(new BigDecimal("25.00"))
            .build();
    OrderItem orderItem11 = orderItemMapper.toEntity(orderItem1);
    OrderItem orderItem22 = orderItemMapper.toEntity(orderItem2);
    Set<OrderItem> orderItems = Set.of(orderItem11,orderItem22);
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
    final UserDto user = UserDto.builder()
            .id(ID)
            .username("limbo")
            .firstName("Limbo")
            .lastName("Limbovich")
            .email("limbo@limbo.ru")
            .phone("0555 001 001")
            .password("password")
            .confirmPassword("password")
            .orders(Set.of(order1, order2))
            .build();
    final UserDto user2 = UserDto.builder()
            .id(2L)
            .username("limbo2")
            .firstName("Limbo2")
            .lastName("Limbovich2")
            .email("limbo2@limbo.ru")
            .phone("0555 001 002")
            .password("password2")
            .confirmPassword("password2")
            .orders(Set.of(order1, order2))
            .build();


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController)
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
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$.items[0].id", is(1)))
                .andExpect(jsonPath("$.items[0].productId", is(2)))
                .andExpect(jsonPath("$.items[0].quantity", is(1)))
                .andExpect(jsonPath("$.items[0].sellingPrice", is(new BigDecimal("25.00"))))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.items[1].id", is(2)))
                .andExpect(jsonPath("$.items[1].productId", is(1)))
                .andExpect(jsonPath("$.items[1].quantity", is(2)))
                .andExpect(jsonPath("$.items[1].sellingPrice", is(new BigDecimal("25.00"))));

        verify(orderService, times(1)).getOrders();
    }

    @Test
    void getOrderById() throws Exception {
        when(orderService.getOrderById(ID)).thenReturn(order1);

        mockMvc.perform(get("/orders/{orderId}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value(1))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.items[0].sellingPrice").value(25))
                .andDo(print());
    }

    /*@Test
    void addItemToOrder() throws Exception {
        Long orderId = 1L;
        OrderItem orderItem = OrderItem.builder()
                .id(3L)
                .productId(1L)
                .quantity(1)
                .sellingPrice(new BigDecimal("30")).build();
        when(orderService.addItem(orderId, any(OrderItemDto.class))).thenReturn(order1);
        mockMvc.perform(post("/orders/{orderId}/items", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(orderItem)))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.asJsonString(order1)));
        verify(orderService, times(1)).addItem(orderId, orderItem1);
    }*/

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
                .id(orderItem1.getId())
                .productId(orderItem1.getProductId())
                .quantity(orderItem1.getQuantity())
                .sellingPrice(orderItem1.getSellingPrice())
                .build();
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
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].sellingPrice").value(25))
                .andExpect(jsonPath("$[1].id").value(2))
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