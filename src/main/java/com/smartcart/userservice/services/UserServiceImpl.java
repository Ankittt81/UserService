package com.smartcart.userservice.services;

import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.repositories.TokenRepository;
import com.smartcart.userservice.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder  bCryptPasswordEncoder;
    private TokenRepository tokenRepository;

    public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            //Redirect to log-in
            return userOptional.get();
        }
      //1 way:  User user=userRepository.save(name,email,bCryptPasswordEncoder.encode(password));
        User user=new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) throws PasswordMisMatchException {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            //redirect to signup
            return null;
        }
        User user=userOptional.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new PasswordMisMatchException("Incorrect Password");
        }
        //login successful
//        //token generate
//        Token token=new Token();
//        token.setUser(user);
//        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));
//
//        Calendar  calendar=Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR,30);
//        Date expiryDate=calendar.getTime();
//        token.setExpiryAt(expiryDate);
//
//        return tokenRepository.save(token);

        //Generate a JWT Token using JJWT library.

        String payload = "{\n" +
                "  \"email\": \"shubham@gmail.com\",\n" +
                "  \"userId\": \"2\",\n" +
                "  \"roles\": [\"STUDENT\"],\n" +
                "  \"expiry\": \"2025-10-05T12:34:56Z\"\n" +
                "}";

        byte[] payloadbytes=payload.getBytes();
        String jwtToken= Jwts.builder().content(payloadbytes).compact();

        return jwtToken;
    }

    @Override
    public User validateToken(String tokenValue) {
        Optional<Token> tokenOptional=tokenRepository.findTokensByTokenValueAndExpiryAtGreaterThan(tokenValue,new Date());
        if(tokenOptional.isEmpty()){
            //Invalid token
        }
        Token token=tokenOptional.get();
        return token.getUser();
    }

    @Override
    //1-way
    public Token logout(String tokenValue) {
        Optional<Token> tokenOptional=tokenRepository.findByTokenValue(tokenValue);
        if(tokenOptional.isEmpty()){
            //Already logout
            return null;
        }
        Token token=tokenOptional.get();

        return tokenRepository.deleteTokenById(token.getId());
    }
    //2-way
    public Token logOut(String tokenValue){
        Optional<Token> tokenOptional=tokenRepository.findByTokenValue(tokenValue);
        if(tokenOptional.isEmpty()){
            return null;
        }
        Token token=tokenOptional.get();
        token.setExpiryAt(new Date());
        tokenRepository.save(token);
        return token;
    }
}
