package com.smartcart.userservice.dtos;

import com.smartcart.userservice.models.Role;
import com.smartcart.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long userId;
    private String name;
    private String email;
    private Set<Role> roles;

    public static UserDto from(User user){
        UserDto userDto=new UserDto();
        userDto.setUserId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
    }
}
