package com.smartcart.userservice.services;

import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.exceptions.UserAlreadyExistException;
import com.smartcart.userservice.exceptions.UserNotFoundException;
import com.smartcart.userservice.mappers.UserMapper;
import com.smartcart.userservice.models.Role;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.repositories.RoleRepository;
import com.smartcart.userservice.repositories.TokenRepository;
import com.smartcart.userservice.repositories.UserRepository;
import com.smartcart.userservice.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder  bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private UserMapper userMapper;
    private RoleRepository roleRepository;
    private JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository, UserMapper userMapper, RoleRepository roleRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            //Redirect to log-in
            throw new UserAlreadyExistException("User already exists");
        }
      //1 way:  User user=userRepository.save(name,email,bCryptPasswordEncoder.encode(password));
        Role userRole=roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("ROLE_USER not found"));
        String hashPassword=bCryptPasswordEncoder.encode(password);
        Set<Role> roles=new HashSet<>();
        roles.add(userRole);
        User user=userMapper.toEntity(name,email,hashPassword,roles);
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password, HttpServletResponse response)  {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            //redirect to signup
            throw new UserNotFoundException();
        }
        User user=userOptional.get();
        if(!bCryptPasswordEncoder.matches(password,user.getPassword())){
            throw new PasswordMisMatchException("Incorrect Password");
        }

        String token= jwtService.generateToken(user);
        Cookie cookie=new Cookie("access_token",token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new String("Login Successful"+token);
    }

    @Override
    public User validateToken(String tokenValue) {
        Long userId=jwtService.validateToken(tokenValue);
        Optional<User> userOptional=userRepository.findById(userId);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userOptional.get();
        return user;
    }

    @Override
    //1-way
    public Token logout(String tokenValue) {
        Optional<Token> tokenOptional = tokenRepository.findByTokenValue(tokenValue);
        if (tokenOptional.isEmpty()) {
            //Already logout
            return null;
        }
        Token token = tokenOptional.get();

        return tokenRepository.deleteTokenById(token.getId());
    }
}
