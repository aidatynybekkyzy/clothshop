package com.aidatynybekkyzy.clothshop.config;

import com.aidatynybekkyzy.clothshop.security.jwt.JWTAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    private static final String AUTH_END_POINT = "/auth/**";
    private static final String CATEGORY_ADMIN = "/categories/admin/**";
    private static final String ORDER_ADMIN = "/orders/admin/**";
    private static final String VENDOR_ADMIN = "/vendors/admin/**";
    private static final String PRODUCT_ADMIN = "/products/admin/**";
    private static final String USER_ADMIN = "/users/admin/**";
    private static final String ADMIN = "ADMIN";

    @Autowired
    public SecurityConfig(JWTAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, LogoutHandler logoutHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .antMatchers(AUTH_END_POINT).permitAll()
                                .antMatchers(HttpMethod.GET, "/vendors/").permitAll()
                                .antMatchers(HttpMethod.GET, "/categories/").permitAll()
                                .antMatchers(HttpMethod.GET, "/products/").permitAll()
                                .antMatchers(HttpMethod.GET, "/users/").permitAll()
                                .antMatchers(HttpMethod.GET, "/orders/**").permitAll()
                               // .antMatchers(HttpMethod.POST, "/admin/**").hasRole(ADMIN)
                                //.antMatchers(VENDOR_ADMIN, USER_ADMIN, CATEGORY_ADMIN, ORDER_ADMIN).hasAuthority(ADMIN)
                                .anyRequest().authenticated()
                )
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) ->
                        SecurityContextHolder.clearContext())
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
