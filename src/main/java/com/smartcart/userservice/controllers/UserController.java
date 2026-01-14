package com.smartcart.userservice.controllers;

import com.smartcart.userservice.dtos.*;
import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.services.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user= userService.signup(signUpRequestDto.getName(),signUpRequestDto.getEmail(),signUpRequestDto.getPassword());

        return UserDto.from(user);
    }

    @PostMapping("/login")
    public TokenDto login(@RequestBody LoginRequestDto loginRequestDto) throws PasswordMisMatchException {
        Token token=userService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

        return TokenDto.from(token);
    }

    @GetMapping("/validate/{token}")
    public UserDto validate(@PathVariable("token") String token){
        User user=userService.validateToken(token);
        return UserDto.from(user);
    }

    @PutMapping("/logout")
    public UserDto logout(@RequestBody LogoutRequestDto logoutRequestDto){
        return null;
    }
}
