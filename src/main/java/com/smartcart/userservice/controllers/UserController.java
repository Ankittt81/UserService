package com.smartcart.userservice.controllers;

import com.smartcart.userservice.dtos.*;
import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
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
    public String login(@RequestBody LoginRequestDto loginRequestDto) throws PasswordMisMatchException {
        String token=userService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        return token;
       // return TokenDto.from(token);
    }

    @GetMapping("/validate/{token}")
    public UserDto validate(@PathVariable("token") String token){
        User user=userService.validateToken(token);
        return UserDto.from(user);
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<LogOutResponseDto> logout(@RequestBody LogoutRequestDto logoutRequestDto){
        Token user=userService.logOut(logoutRequestDto.getTokenValue());
        LogOutResponseDto logOutResponseDto=new LogOutResponseDto();
        if(user==null){
            logOutResponseDto.setMessage("Already logged Out!");
        }
        else logOutResponseDto.setMessage("Logged Out!");
        return ResponseEntity.ok (logOutResponseDto);
    }
}
