package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.JsonUtils;
import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.ProductDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.exceptionHandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.model.OrderStatus;
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
    final OrderDto order1 = OrderDto.builder()
            .id(ID)
            .items(List.of(product1))
            .userId(1L)
            .build();
    final OrderDto order2 = OrderDto.builder()
            .id(2L)
            .items(List.of(product2))
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
                .andExpect(jsonPath("$[0].items[0].id", is(1)))
                .andExpect(jsonPath("$[0].items[0].name", is("Adidas")))
                .andExpect(jsonPath("$[0].items[0].price", is(1000)))
                .andExpect(jsonPath("$[0].items[0].quantity", is(5)))
                .andExpect(jsonPath("$[0].items[0].categoryId", is(1)))
                .andExpect(jsonPath("$[0].items[0].vendorId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].items[0].id", is(2)))
                .andExpect(jsonPath("$[1].items[0].name", is("Nike")))
                .andExpect(jsonPath("$[1].items[0].price", is(500)))
                .andExpect(jsonPath("$[1].items[0].quantity", is(3)))
                .andExpect(jsonPath("$[1].items[0].categoryId", is(1)))
                .andExpect(jsonPath("$[1].items[0].vendorId", is(1)));


        verify(orderService, times(1)).getOrders();
    }

    @Test
    void getOrderById() throws Exception {
        when(orderService.getOrderById(ID)).thenReturn(order1);

        mockMvc.perform(get("/orders/{orderId}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order1.getId()))
                .andExpect(jsonPath("$.items[0].id", is(1)))
                .andExpect(jsonPath("$.items[0].name", is("Adidas")))
                .andExpect(jsonPath("$.items[0].price", is(1000)))
                .andExpect(jsonPath("$.items[0].quantity", is(5)))
                .andExpect(jsonPath("$.items[0].categoryId", is(1)))
                .andExpect(jsonPath("$.items[0].vendorId", is(1)))
                .andDo(print());
    }

    @Test
    void addItemToOrder() throws Exception {
        Long orderId = 1L;
        when(orderService.addItem(orderId, product1)).thenReturn(order1);
        mockMvc.perform(post("/orders/{orderId}/items", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(product1)))
                .andExpect(status().isOk())
                .andExpect(content().json(JsonUtils.asJsonString(order1)));
        verify(orderService, times(1)).addItem(orderId, product1);
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
        when(orderService.getItemOrder(orderId, itemId)).thenReturn(product1);
        mockMvc.perform(get("/orders/{orderId}/items/{itemId}", orderId, itemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product1.getId()))
                .andExpect(jsonPath("$.name").value(product1.getName()))
                .andExpect(jsonPath("$.price").value(product1.getPrice()))
                .andExpect(jsonPath("$.quantity").value(product1.getQuantity()))
                .andExpect(jsonPath("$.categoryId").value(product1.getCategoryId()))
                .andExpect(jsonPath("$.vendorId").value(product1.getVendorId()))
                .andDo(print());

    }

    @Test
    void getOrderItems() throws Exception {
        Long orderId = 1L;
        when(orderService.getAllOrderItems(orderId)).thenReturn(productDtos);
        mockMvc.perform(get("/orders/{orderId}/items", orderId))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("Adidas")))
                .andExpect(jsonPath("$.[0].price", is(1000)))
                .andExpect(jsonPath("$.[0].quantity", is(5)))
                .andExpect(jsonPath("$.[0].categoryId", is(1)))
                .andExpect(jsonPath("$.[0].vendorId", is(1)))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[1].name", is("Nike")))
                .andExpect(jsonPath("$.[1].price", is(500)))
                .andExpect(jsonPath("$.[1].quantity", is(3)))
                .andExpect(jsonPath("$.[1].categoryId", is(1)))
                .andExpect(jsonPath("$.[1].vendorId", is(1)));
    }

    @Test
    void deleteOrderItem() throws Exception {
        Long orderId = 1L;
        Long itemId = 1L;
        mockMvc.perform(delete("/orders/{orderId}/items/{itemId}", orderId, itemId))
                .andExpect(status().isOk())
                .andDo(print());
        verify(orderService, times(1)).deleteItemOrder(orderId, itemId);
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