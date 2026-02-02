package com.smartcart.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends  BaseModel{
    private String name;
    private String email;
    private String password;
    @ManyToMany
    private Set<Role> roles;
    @Enumerated(EnumType.STRING)
    private Status status;
}


/*
   1          M
User ----- Role => M:M
  M          1

 */

