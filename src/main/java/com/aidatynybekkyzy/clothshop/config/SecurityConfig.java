package com.aidatynybekkyzy.clothshop.config;

import com.aidatynybekkyzy.clothshop.security.jwt.JWTAuthFilter;
import com.aidatynybekkyzy.clothshop.security.jwt.JwtUserDetailsService;
import com.aidatynybekkyzy.clothshop.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableSwagger2
public class SecurityConfig {
    private final JWTAuthFilter jwtAuthFilter;
    private final LogoutHandler logoutHandler;
    private final JwtUserDetailsService jwtUserDetailsService;

    private static final String AUTH_END_POINT = "/auth/**";
    private static final String CATEGORY_ADMIN = "/categories/admin/**";
    private static final String ORDER_ADMIN = "/orders/admin/**";
    private static final String VENDOR_ADMIN = "/vendors/admin/**";
    private static final String PRODUCT_ADMIN = "/products/admin/**";
    private static final String USER_ADMIN = "/users/admin/**";
    private static final String ADMIN = "ADMIN";

    @Autowired
    public SecurityConfig( JWTAuthFilter jwtAuthFilter, LogoutHandler logoutHandler, JwtUserDetailsService jwtUserDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.logoutHandler = logoutHandler;
        this.jwtUserDetailsService = jwtUserDetailsService;
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
                                .antMatchers("/swagger-ui").permitAll()
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
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(jwtUserDetailsService.userDetailsService());
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
