package com.smartcart.userservice.exceptions;


public class PasswordMisMatchException extends RuntimeException{

    public PasswordMisMatchException(String message){
        super(message);
    }
}
