package com.smartcart.userservice.controllers;

import com.smartcart.userservice.dtos.*;
import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.mappers.UserMapper;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {
    private UserService userService;
    private UserMapper  userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService=userService;
        this.userMapper=userMapper;
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        User user= userService.signup(signUpRequestDto.getName(),signUpRequestDto.getEmail(),signUpRequestDto.getPassword());

        return userMapper.toDto(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto,HttpServletResponse response) throws PasswordMisMatchException {
        String token=userService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword(),response);
        return token;
       // return TokenDto.from(token);
    }

    @GetMapping("/validate/{token}")
    public UserDto validate(@PathVariable("token") String token){
        User user=userService.validateToken(token);
        return userMapper.toDto(user);
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<LogOutResponseDto> logout(@RequestBody LogoutRequestDto logoutRequestDto){
        Token user=userService.logout(logoutRequestDto.getTokenValue());
        LogOutResponseDto logOutResponseDto=new LogOutResponseDto();
        if(user==null){
            logOutResponseDto.setMessage("Already logged Out!");
        }
        else logOutResponseDto.setMessage("Logged Out!");
        return ResponseEntity.ok (logOutResponseDto);
    }
}
