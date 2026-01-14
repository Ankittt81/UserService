package com.smartcart.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends  BaseModel{
    private String name;
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles;
}


/*
   1          M
User ----- Role => M:M
  M          1

 */

