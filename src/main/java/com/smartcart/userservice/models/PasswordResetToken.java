package com.smartcart.userservice.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class PasswordResetToken extends BaseModel{
    private String email;
    private String hashOtp;
    private LocalDateTime expiryTime;
    private boolean verified;
    private Integer attempts;
}
