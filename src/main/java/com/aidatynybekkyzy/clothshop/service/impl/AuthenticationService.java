package com.aidatynybekkyzy.clothshop.service.impl;

import com.aidatynybekkyzy.clothshop.dto.AuthenticationRequestDto;
import com.aidatynybekkyzy.clothshop.dto.UserDto;
import com.aidatynybekkyzy.clothshop.exception.EntityNotFoundException;
import com.aidatynybekkyzy.clothshop.exception.PasswordIncorrectException;
import com.aidatynybekkyzy.clothshop.mapper.UserMapper;
import com.aidatynybekkyzy.clothshop.model.Token;
import com.aidatynybekkyzy.clothshop.model.TokenType;
import com.aidatynybekkyzy.clothshop.model.User;
import com.aidatynybekkyzy.clothshop.model.response.AuthenticationResponseDto;
import com.aidatynybekkyzy.clothshop.repository.RoleRepository;
import com.aidatynybekkyzy.clothshop.repository.TokenRepository;
import com.aidatynybekkyzy.clothshop.repository.UserRepository;
import com.aidatynybekkyzy.clothshop.security.jwt.JwtTokenProvider;
import com.aidatynybekkyzy.clothshop.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Set;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;

    private final TokenRepository tokenRepository;

    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserDto register(UserDto request) throws PasswordIncorrectException {
        log.info("Registering new user with email " + request.getEmail());
        if (!request.getConfirmPassword().equals(request.getPassword())) {
            throw new PasswordIncorrectException("Passwords do not match");
        }
        var user = User.builder()
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .confirmPassword(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Set.of(roleRepository.findByRoleName("USER")))
                .build();
        userRepository.save(user);
        log.info("Saved user with email " + request.getEmail());
        return  userMapper.toDto(user);
    }

    public AuthenticationResponseDto authenticate(@NotNull AuthenticationRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword())
            );
            User user = userService.findByUsername(request.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("User does not exist"));
            var jwtToken = jwtTokenProvider.generateToken(user, user.getRole());
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            return AuthenticationResponseDto.builder()
                    .accessToken(jwtToken)
                    .username(request.getUsername())
                    .build();
        } catch (BadCredentialsException exception){
            throw new PasswordIncorrectException("Invalid password");
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}
