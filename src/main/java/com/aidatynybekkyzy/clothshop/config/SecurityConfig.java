package com.aidatynybekkyzy.clothshop.config;

import com.aidatynybekkyzy.clothshop.security.jwt.JWTAuthFilter;
import com.aidatynybekkyzy.clothshop.service.UserService;
import com.aidatynybekkyzy.clothshop.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    private final UserServiceImpl userService;
    private final JWTAuthFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;

    private static final String AUTH_END_POINT = "/auth/**";
    private static final String CATEGORY_ADMIN = "/categories/admin/**";
    private static final String ORDER_ADMIN = "/orders/admin/**";
    private static final String VENDOR_ADMIN = "/vendors/admin/**";
    private static final String PRODUCT_ADMIN = "/products/admin/**";
    private static final String USER_ADMIN = "/users/admin/**";
    private static final String ADMIN = "ADMIN";

    @Autowired
    public SecurityConfig(UserServiceImpl userService, JWTAuthFilter jwtAuthFilter , LogoutHandler logoutHandler) {
        this.userService = userService;
        this.jwtAuthFilter = jwtAuthFilter;
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
                                .antMatchers(HttpMethod.POST, "/products/admin/createProduct").hasRole(ADMIN)
                                .antMatchers(VENDOR_ADMIN, USER_ADMIN, CATEGORY_ADMIN, ORDER_ADMIN).hasRole(ADMIN)
                                .anyRequest().authenticated()
                )
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) ->
                        SecurityContextHolder.clearContext())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



}
