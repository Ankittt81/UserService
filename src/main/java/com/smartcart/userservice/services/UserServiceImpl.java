package com.smartcart.userservice.services;

import com.smartcart.userservice.events.ResetPasswordEvent;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder  bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private UserMapper userMapper;
    private RoleRepository roleRepository;
    private JwtService jwtService;
    private OtpService otpService;
    private KafkaProducerService kafkaProducerService;


    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                           TokenRepository tokenRepository, UserMapper userMapper,
                           RoleRepository roleRepository, JwtService jwtService,KafkaProducerService kafkaProducerService,
                           OtpService otpService) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.otpService = otpService;
        this.kafkaProducerService = kafkaProducerService;
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

        return new String("Login Successfull "+token);
    }

    public void resetPasswordRequest(String email){
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        //generate otp
        String otp=otpService.generateAndSaveOtp(email);

        //create event
        ResetPasswordEvent resetPasswordEvent=new ResetPasswordEvent();
        resetPasswordEvent.setEmail(email);
        resetPasswordEvent.setOtp(otp);

        //send via kafka
        kafkaProducerService.sendResetPasswordEvent(resetPasswordEvent);
    }

    public void checkOtp(String email,String otp){
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        otpService.validateOtp(email,otp);
    }

    @Transactional
    public void resetPasswordConfirm(String email,String newPassword){
       otpService.verifyOtp(email);
        // fetch user
        Optional<User> userOptional =userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserNotFoundException();
        }
        User user=userOptional.get();
        // update password
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(user);

    }

    @Override
    public User validateToken(String tokenValue) {
        Claims claims =jwtService.validateToken(tokenValue);
        Long userId=Long.valueOf(claims.get("userId").toString());
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
