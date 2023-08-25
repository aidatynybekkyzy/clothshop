package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.JsonUtils;
import com.aidatynybekkyzy.clothshop.dto.OrderDto;
import com.aidatynybekkyzy.clothshop.dto.OrderItemDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.exceptionHandler.GlobalExceptionHandler;
import com.aidatynybekkyzy.clothshop.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userService;
    MockMvc mockMvc;
    private final static long ID = 1L;

    OrderItemDto orderItem1 = OrderItemDto.builder()
            .quantity(2)
            .productId(1L)
            .sellingPrice(new BigDecimal(25))
            .build();
    OrderItemDto orderItem2 = OrderItemDto.builder()
            .quantity(2)
            .productId(2L)
            .sellingPrice(new BigDecimal(25))
            .build();

    final OrderDto order1 = OrderDto.builder()
            .id(ID)
            .items(Set.of(orderItem1))
            .userId(1L)
            .build();
    final OrderDto order2 = OrderDto.builder()
            .id(2L)
            .items(Set.of(orderItem2))
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
    final List<UserDto> users = List.of(user, user2);

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @WithMockUser(username = "aidatyn@gmail.com", roles = "ADMIN")
    void createUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/users/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("limbo")))
                .andExpect(jsonPath("$.firstName", is("Limbo")))
                .andExpect(jsonPath("$.lastName", is("Limbovich")))
                .andExpect(jsonPath("$.email", is("limbo@limbo.ru")))
                .andExpect(jsonPath("$.phone", is("0555 001 001")))
                .andExpect(jsonPath("$.password", is("password")))
                .andExpect(jsonPath("$.confirmPassword", is("password")));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(ID)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("limbo")))
                .andExpect(jsonPath("$.firstName", is("Limbo")))
                .andExpect(jsonPath("$.lastName", is("Limbovich")))
                .andExpect(jsonPath("$.email", is("limbo@limbo.ru")))
                .andExpect(jsonPath("$.phone", is("0555 001 001")))
                .andDo(print());
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users/admin/getUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("limbo")))
                .andExpect(jsonPath("$[0].firstName", is("Limbo")))
                .andExpect(jsonPath("$[0].lastName", is("Limbovich")))
                .andExpect(jsonPath("$[0].email", is("limbo@limbo.ru")))
                .andExpect(jsonPath("$[0].phone", is("0555 001 001")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("limbo2")))
                .andExpect(jsonPath("$[1].firstName", is("Limbo2")))
                .andExpect(jsonPath("$[1].lastName", is("Limbovich2")))
                .andExpect(jsonPath("$[1].email", is("limbo2@limbo.ru")))
                .andExpect(jsonPath("$[1].phone", is("0555 001 002")))
                .andDo(print());


    }

    @Test
    @WithMockUser(username = "aidatyn@gmail.com", roles = "ADMIN")
    void updateUser() throws Exception {
        final UserDto updatedUser = UserDto.builder()
                .id(ID)
                .username("updated limbo")
                .firstName("updated Limbo2")
                .lastName("updated Limbovich2")
                .email("limbo2@limbo.ru")
                .phone("0555 001 002")
                .password("password2")
                .confirmPassword("password2")
                .orders(Set.of(order1, order2))
                .build();
        when(userService.updateUser(eq(ID), any(UserDto.class))).thenReturn(updatedUser);
        mockMvc.perform(patch("/users/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(user))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is(updatedUser.getUsername())))
                .andExpect(jsonPath("$.firstName", is(updatedUser.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedUser.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedUser.getEmail())))
                .andExpect(jsonPath("$.phone", is(updatedUser.getPhone())));
        verify(userService, times(1)).updateUser(eq(ID), any(UserDto.class));
    }

    @Test
    void deleteUser() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/users/admin/{id}", id))
                .andExpect(status().isNoContent())
                .andDo(print());
        verify(userService, times(1)).deleteUser(id);
    }

    @Test
    void getUserOrders() throws Exception {
        Long userId = 1L;
        when(userService.getUserOrders(userId)).thenReturn(orders);
        mockMvc.perform(get("/users/{userId}/orders", userId)
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

        verify(userService, times(1)).getUserOrders(userId);

    }

    @Test
    void createOrderForUser() throws Exception {
        Long userId = 1L;

        OrderDto createdOrder = OrderDto.builder()
                .items(Set.of(orderItem1))
                .userId(userId)
                .build();

        when(userService.createOrderForCustomer(eq(userId), any(OrderDto.class))).thenReturn(createdOrder);
        mockMvc.perform(post("/users/{userId}/orders", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(order1))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.items[0].quantity", is(2)))
                .andExpect(jsonPath("$.items[0].productId", is(1)))
                .andExpect(jsonPath("$.items[0].sellingPrice", is(25)));

        verify(userService, times(1)).createOrderForCustomer(eq(userId), any(OrderDto.class));
    }


}