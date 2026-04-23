package com.smartcart.userservice.controllers;

import com.smartcart.userservice.dtos.*;
import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.mappers.UserMapper;

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
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return ResponseEntity.ok(userService.signup(signUpRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto loginRequestDto,HttpServletResponse response) throws PasswordMisMatchException {
        String token=userService.login(loginRequestDto,response);
        return ResponseEntity.ok(new ApiResponse("Success ",token));

       // return TokenDto.from(token);
    }

    @GetMapping("/validate/{token}")
    public ResponseEntity<UserDto> validate(@PathVariable("token") String token){
        User user=userService.validateToken(token);
        return ResponseEntity.ok(userMapper.toDto(user));
    }

//    @Transactional
//    @PostMapping("/logout")
//    public ResponseEntity<LogOutResponseDto> logout(@RequestBody LogoutRequestDto logoutRequestDto){
//        Token user=userService.logout(logoutRequestDto.getTokenValue());
//        LogOutResponseDto logOutResponseDto=new LogOutResponseDto();
//        if(user==null){
//            logOutResponseDto.setMessage("Already logged Out!");
//        }
//        else logOutResponseDto.setMessage("Logged Out!");
//        return ResponseEntity.ok (logOutResponseDto);
//    }

    @PostMapping("/reset-password/request")
    public ResponseEntity<String> resetPasswordrequest(@RequestBody ResetPasswordDto dto){
        userService.resetPasswordRequest(dto.getEmail());
        return ResponseEntity.ok("Request raised");
    }

    @PostMapping("/reset-password/validate")
    public ResponseEntity<String> checkotp(@RequestBody ResetPasswordDto dto){
        userService.checkOtp(dto.getEmail(), dto.getOtp());
        return ResponseEntity.ok("otp checked!");
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPasswordConfirm(@RequestBody  ResetPasswordDto dto){
        userService.resetPasswordConfirm(dto.getEmail(), dto.getNewPassword());
        return ResponseEntity.ok("Password has been reset!");
    }
}
