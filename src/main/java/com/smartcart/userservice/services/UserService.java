package com.smartcart.userservice.services;


import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;

public interface UserService {
   User signup(String name,String email,String password);

   Token login(String email,String password) throws PasswordMisMatchException;

   User validateToken(String tokenValue);

   User logout(String email);
}
