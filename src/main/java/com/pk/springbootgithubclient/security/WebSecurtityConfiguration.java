package com.pk.springbootgithubclient.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurtityConfiguration  {
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("user")
            .roles("USER")
            .build();

        UserDetails actuator = User.withDefaultPasswordEncoder()
            .username("actuator")
            .password("actuator")
            .roles("ACTUATOR")
            .build();

        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin")
            .roles("USER", "ACTUATOR", "ADMIN")
            .build();

        return new MapReactiveUserDetailsService(user, admin, actuator);
    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        return http
            .authorizeExchange()
                .pathMatchers("/actuator/**").hasRole("ACTUATOR")
                .pathMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                .anyExchange().hasAnyRole("USER", "ADMIN")
            .and().httpBasic()
            .and().build();
    }
}