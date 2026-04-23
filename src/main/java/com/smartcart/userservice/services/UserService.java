package com.smartcart.userservice.services;


import com.smartcart.userservice.dtos.LoginRequestDto;
import com.smartcart.userservice.dtos.SignUpRequestDto;
import com.smartcart.userservice.dtos.UserDto;
import com.smartcart.userservice.exceptions.PasswordMisMatchException;

import com.smartcart.userservice.models.User;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
   UserDto signup(SignUpRequestDto dto);

   String login(LoginRequestDto loginRequestDto, HttpServletResponse response) throws PasswordMisMatchException;

   User validateToken(String tokenValue);

   // Token logout(String tokenValue);
    void resetPasswordRequest(String email);
    void checkOtp(String email,String otp);
    void resetPasswordConfirm(String email,String newPassword);
}
