package com.example.raiffeisen.security.jwt;


import static com.example.raiffeisen.security.user.Permission.USER_CREATE;
import static com.example.raiffeisen.security.user.Permission.USER_DELETE;
import static com.example.raiffeisen.security.user.Permission.USER_READ;
import static com.example.raiffeisen.security.user.Permission.USER_UPDATE;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtAuthFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;

  private static final String [] WHITE_LIST_URL = {"/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**"};
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req ->
            req.requestMatchers(WHITE_LIST_URL).permitAll()
                .requestMatchers(POST, "/api/v1/account/**").hasAnyAuthority(USER_CREATE.getPermission())
                .requestMatchers(GET, "/api/v1/account/**").hasAnyAuthority(USER_READ.getPermission())
                .requestMatchers(DELETE, "/api/v1/account/**").hasAnyAuthority(USER_DELETE.getPermission())
                .requestMatchers(PUT, "/api/v1/account/**").hasAnyAuthority(USER_UPDATE.getPermission())
                .anyRequest()
                .authenticated())
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .headers(AbstractHttpConfigurer::disable)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }
}