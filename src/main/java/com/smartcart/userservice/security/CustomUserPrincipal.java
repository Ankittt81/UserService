package com.smartcart.userservice.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUserPrincipal {
    private Long userId;
    private String email;

    public CustomUserPrincipal(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }
}
