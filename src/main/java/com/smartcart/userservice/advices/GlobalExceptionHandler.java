package com.smartcart.userservice.advices;

import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<String> handlePasswordMisMatch(){
        return new ResponseEntity<>("Incorrect Password", HttpStatus.UNAUTHORIZED);
    }
}
