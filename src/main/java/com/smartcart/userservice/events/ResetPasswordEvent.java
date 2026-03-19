package com.smartcart.userservice.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordEvent {
    String email;
    String otp;
}
