package com.aidatynybekkyzy.clothshop.config;

import com.aidatynybekkyzy.clothshop.security.jwt.JWTAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    private static final String AUTH_END_POINT = "/auth/**";
    private static final String CATEGORY_END_POINT = "/categories/**";
    private static final String ORDER_END_POINT = "/orders/**";
    private static final String VENDOR_END_POINT = "/vendors/**";
    private static final String PRODUCT_END_POINT = "/products/**";
    private static final String USER_END_POINT = "/users/**";
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, VENDOR_END_POINT,CATEGORY_END_POINT,PRODUCT_END_POINT,USER_END_POINT).permitAll()
                .antMatchers(AUTH_END_POINT).permitAll()
                .antMatchers(CATEGORY_END_POINT,VENDOR_END_POINT,PRODUCT_END_POINT,ORDER_END_POINT, USER_END_POINT)
                .authenticated()
                .anyRequest()
                .hasAnyRole(USER, ADMIN)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) ->
                        SecurityContextHolder.clearContext());

        return http.build();
    }
}
