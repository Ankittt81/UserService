package com.smartcart.userservice.services;

import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.repositories.TokenRepository;
import com.smartcart.userservice.repositories.UserRepository;
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
    public Token login(String email, String password) throws PasswordMisMatchException {
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
        //token generate
        Token token=new Token();
        token.setUser(user);
        token.setTokenValue(RandomStringUtils.randomAlphanumeric(128));

        Calendar  calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,30);
        Date expiryDate=calendar.getTime();
        token.setExpiryAt(expiryDate);

        return tokenRepository.save(token);
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
    public User logout(String email) {
        return null;
    }
}
