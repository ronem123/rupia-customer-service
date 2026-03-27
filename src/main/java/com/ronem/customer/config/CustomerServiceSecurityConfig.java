/**
 * Author: Ram Mandal
 * Created on @System: Apple M1 Pro
 * User:rammandal
 * Date:23/03/2026
 * Time:11:31
 */


package com.ronem.customer.config;

import com.ronem.rupiasecuritylib.constants.PublicPaths;
import com.ronem.rupiasecuritylib.filter.GatewayAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CustomerServiceSecurityConfig {
    private final GatewayAuthenticationFilter authenticationFilter;
    private final HandlerExceptionResolver exceptionResolver;

    public CustomerServiceSecurityConfig(
            GatewayAuthenticationFilter authenticationFilter,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.authenticationFilter = authenticationFilter;
        this.exceptionResolver = exceptionResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> {
                            PublicPaths.AUTH_PUBLIC_PATHS.forEach(p ->
                                    auth.requestMatchers(p).permitAll()
                            );

                            auth.anyRequest().authenticated();
                        }
                )
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer
                                .authenticationEntryPoint((request, response, authException) ->
                                        exceptionResolver.resolveException(request, response, null, authException))
                                .accessDeniedHandler((request, response, accessDeniedException) ->
                                        exceptionResolver.resolveException(request, response, null, accessDeniedException))
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}