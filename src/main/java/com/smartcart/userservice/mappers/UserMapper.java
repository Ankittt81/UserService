package com.smartcart.userservice.mappers;

import com.smartcart.userservice.models.Role;
import com.smartcart.userservice.models.Status;
import com.smartcart.userservice.models.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {

    public User toEntity(String name, String email, String password, Set<Role> roles) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(roles);
        user.setStatus(Status.ACTIVE);
        return user;
    }
}
