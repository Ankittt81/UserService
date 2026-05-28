package com.smartcart.userservice.security;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CustomUserPrincipal {
    private Long userId;
    private String email;
    List<String> roles;

    public CustomUserPrincipal(Long userId, String email,List<String> roles) {
        this.userId = userId;
        this.email = email;
        this.roles = roles;
    }
}
