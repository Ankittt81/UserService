package com.smartcart.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TokenDto {
    private String tokenValue;
    private Date expiryAt;
}
