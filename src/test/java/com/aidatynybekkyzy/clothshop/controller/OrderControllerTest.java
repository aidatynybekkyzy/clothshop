package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.*;
import com.aidatynybekkyzy.clothshop.exception.exceptionHandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.service.impl.OrderServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class OrderControllerTest {
    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderServiceImpl orderService;
    MockMvc mockMvc;
    private final static long ID = 1L;
    final VendorDto vendorDto = VendorDto.builder()
            .id(ID)
            .vendorName("Test vendor name")
            .build();
    final CategoryDto shoes = CategoryDto.builder()
            .id(ID)
            .categoryName("Category 1").build();
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
    final List<UserDto> users = List.of(user, user2);

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
    void addItemToOrder() {

    }

    @Test
    void cancelOrder() {
    }

    @Test
    void deleteOrderById() {
    }

    @Test
    void getOrderItem() {
    }

    @Test
    void getOrderItems() {
    }

    @Test
    void deleteOrderItem() {
    }

    @Test
    void purchaseOrder() {
    }

    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}