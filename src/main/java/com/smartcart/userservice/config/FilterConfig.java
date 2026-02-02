package com.smartcart.userservice.config;

import com.smartcart.userservice.security.JwtFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    private JwtFilter  jwtFilter;

    public FilterConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(){
        FilterRegistrationBean<JwtFilter> registration=new FilterRegistrationBean<>();

        registration.setFilter(jwtFilter);
        registration.addUrlPatterns("/products/*");

        registration.setOrder(1);
        return registration;
    }
}
