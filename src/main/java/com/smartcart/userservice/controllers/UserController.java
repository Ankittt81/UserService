package com.smartcart.userservice.controllers;

import com.smartcart.userservice.dtos.*;
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
    public TokenDto login(@RequestBody LoginRequestDto loginRequestDto){
        return null;
    }

    @GetMapping("/validate/{token}")
    public UserDto validate(@PathVariable("token") String token){
        return null;
    }

    @PutMapping("/logout")
    public UserDto logout(@RequestBody LogoutRequestDto logoutRequestDto){
        return null;
    }
}
