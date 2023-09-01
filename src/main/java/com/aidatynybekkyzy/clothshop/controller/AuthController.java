package com.aidatynybekkyzy.clothshop.controller;

import com.aidatynybekkyzy.clothshop.dto.AuthenticationRequestDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.PasswordIncorrectException;
import com.aidatynybekkyzy.clothshop.model.response.AuthenticationResponseDto;
import com.aidatynybekkyzy.clothshop.service.impl.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@ApiOperation("Auth controller")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Register new user")
    public ResponseEntity<UserDto> register(
            @RequestBody @Valid UserDto registerDto) throws PasswordIncorrectException {
        log.info("Doing registration of user " + registerDto.getEmail());
        return ResponseEntity.ok(authenticationService.register(registerDto));
    }

    @PostMapping("/login")
    @ApiOperation("Login user")
    public ResponseEntity<AuthenticationResponseDto> authenticate(
            @RequestBody @Valid  AuthenticationRequestDto authenticationRequestDto) {
        log.info("Doing authentication of user " + authenticationRequestDto.getUsername());
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequestDto));
    }

    @PostMapping("/logout")
    @ApiOperation("Logout user")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
