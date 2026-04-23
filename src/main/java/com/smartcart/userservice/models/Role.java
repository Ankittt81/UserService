package com.smartcart.userservice.models;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseModel{
    @Column(unique = true,nullable=false)
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
}
