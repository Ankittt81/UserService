package com.smartcart.userservice.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.smartcart.userservice.models.Role;
import com.smartcart.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@JsonPropertyOrder({
        "userId",
        "name",
        "email",
        "roles"
})
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private List<String> roles;

}
