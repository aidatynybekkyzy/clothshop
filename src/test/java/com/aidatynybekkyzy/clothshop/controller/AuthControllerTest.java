package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.JsonUtils;
import com.aidatynybekkyzy.clothshop.dto.AuthenticationRequestDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.model.Role;
import com.aidatynybekkyzy.clothshop.model.User;
import com.aidatynybekkyzy.clothshop.model.response.AuthenticationResponseDto;
import com.aidatynybekkyzy.clothshop.security.jwt.JwtTokenProvider;
import com.aidatynybekkyzy.clothshop.service.UserService;
import com.aidatynybekkyzy.clothshop.service.common.ResponseErrorValidation;
import com.aidatynybekkyzy.clothshop.service.impl.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationService authenticationService;
    @Mock
    ResponseErrorValidation responseErrorValidation;

    @Autowired
    WebApplicationContext context;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @DisplayName("Register new user")
    void testRegister() throws Exception {
        UserDto requestDto = new UserDto();
        requestDto.setFirstName("User");
        requestDto.setLastName("Test");
        requestDto.setEmail("user@test.com");
        requestDto.setPassword("password");
        requestDto.setConfirmPassword("password");

        User userEntity = new User();
        userEntity.setFirstName(requestDto.getFirstName());
        userEntity.setLastName(requestDto.getLastName());
        userEntity.setEmail(requestDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userEntity.setConfirmPassword(passwordEncoder.encode(requestDto.getConfirmPassword()));
        userEntity.setRole(Set.of(Role.builder().roleName("USER").build()));

        given(authenticationService.register(any(UserDto.class))).willReturn(requestDto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("user@test.com")))
                .andExpect(jsonPath("$.firstName", is("User")))
                .andExpect(jsonPath("$.lastName", is("Test")))
                .andDo(print());

        verify(authenticationService, times(1)).register(any(UserDto.class));
    }

    @Test
    @DisplayName("Login  user")
    public void testAuthenticate() throws Exception {

        String email = "john.doe@example.com";
        String password = "password123";
        String jwtToken = "";

        AuthenticationRequestDto requestDto = new AuthenticationRequestDto();
        requestDto.setUsername(email);
        requestDto.setPassword(password);

        User userEntity = new User();
        userEntity.setId(1L);
        userEntity.setEmail(email);

        Role role = new Role();
        role.setRoleName("ADMIN");
        userEntity.setRole(Set.of(role));

        AuthenticationResponseDto responseDto = new AuthenticationResponseDto();
        responseDto.setAccessToken(jwtToken);

        when(userService.findByUsername(requestDto.getUsername())).thenReturn(Optional.of(userEntity));
        when(jwtTokenProvider.generateToken(userEntity, userEntity.getRole())).thenReturn(jwtToken);
        when(authenticationService.authenticate(requestDto)).thenReturn(responseDto);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(responseDto.getAccessToken()))
                .andDo(print());
    }

    @Test
    @DisplayName("Logout user")
    void testLogout() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}