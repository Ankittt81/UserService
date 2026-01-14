package com.smartcart.userservice.controllers;



import com.smartcart.userservice.dtos.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return null;
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
