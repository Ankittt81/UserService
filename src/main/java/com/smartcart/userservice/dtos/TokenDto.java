//package com.smartcart.userservice.dtos;
//
//import com.smartcart.userservice.models.Token;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.Date;
//
//@Getter
//@Setter
//public class TokenDto {
//    private String tokenValue;
//    private Date expiryAt;
//    private String email;
//
//    public static TokenDto from(Token token){
//        TokenDto tokenDto=new TokenDto();
//        tokenDto.setExpiryAt(token.getExpiryAt());
//        tokenDto.setTokenValue(token.getTokenValue());
//        tokenDto.setEmail(token.getUser().getEmail());
//
//        return tokenDto;
//    }
//}
