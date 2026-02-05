package com.smartcart.userservice.services;


import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
   User signup(String name,String email,String password);

   String login(String email, String password, HttpServletResponse response) throws PasswordMisMatchException;

   User validateToken(String tokenValue);

    Token logout(String tokenValue);
}
