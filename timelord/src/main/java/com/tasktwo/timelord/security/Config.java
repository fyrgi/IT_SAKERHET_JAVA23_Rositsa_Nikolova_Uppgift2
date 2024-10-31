package com.tasktwo.timelord.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//.requestMatchers("/authenticate").permitAll()
//                .requestMatchers("/v1/users/**").authenticated()
@Configuration
public class Config {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //.requestMatchers("/v1/users/**").permitAll() ensure the users access to every mapping under v1/users
        // CSRF removes the mandatory user / generated password login from Spring Security
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("").authenticated()
                        //.anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
