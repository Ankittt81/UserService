package com.smartcart.userservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address extends BaseModel {
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String mobile;

    private String alternateMobile;

    @Column(nullable = false)
    private String houseNo;
    @Column(nullable = false)
    private String area;

    private String landmark;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    @Column(nullable = false)
    private String pincode;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;
    private String customLabel;

    private Boolean isDefault = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;
}
