package com.smartcart.userservice.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Role extends BaseModel{
    @Column(unique = true,nullable=false)
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
}
