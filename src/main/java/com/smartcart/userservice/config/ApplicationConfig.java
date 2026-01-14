package com.smartcart.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ApplicationConfig {

    @Bean
    public BCryptPasswordEncoder  getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                   .cors(cors -> cors.disable());
        httpSecurity.authorizeHttpRequests(
                authorize -> authorize.anyRequest().permitAll()
        );

        return httpSecurity.build();
    }
}
