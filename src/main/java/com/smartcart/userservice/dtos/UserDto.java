package com.smartcart.userservice.dtos;

import com.smartcart.userservice.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private List<Role> roles;
}
