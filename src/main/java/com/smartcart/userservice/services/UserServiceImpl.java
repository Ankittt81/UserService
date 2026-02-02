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
import com.smartcart.userservice.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.RandomStringUtils;
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
    private JwtUtil  jwtUtil;

    public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,TokenRepository tokenRepository,UserMapper userMapper,RoleRepository roleRepository,JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
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
    public String login(String email, String password)  {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            //redirect to signup
            throw new UserNotFoundException();
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

//        String payload = "{\n" +
//                "  \"email\": \"shubham@gmail.com\",\n" +
//                "  \"userId\": \"2\",\n" +
//                "  \"roles\": [\"STUDENT\"],\n" +
//                "  \"expiry\": \"2025-10-05T12:34:56Z\"\n" +
//                "}";

//        byte[] payloadbytes=payload.getBytes();
//        String jwtToken= Jwts.builder().content(payloadbytes).compact();
        //JWT
//        HashMap<String,Object> claims=new HashMap<>();
//        claims.put("iss","authservice");
//        claims.put("email",user.getEmail());
//        claims.put("userId",user.getId());
//        Calendar calendar=Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, 30);
//        Date expiryDate = calendar.getTime();
//        claims.put("exp",expiryDate);
//        claims.put("roles",user.getRoles());
//        String token=Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS256,"AuthService").compact();

        return jwtUtil.generateToken(user);
    }

    @Override
    public User validateToken(String tokenValue) {
        JwtParser jwtParser=Jwts.parserBuilder().setSigningKey("AuthService").build();
        Claims claims=jwtParser.parseClaimsJws(tokenValue).getBody();
        System.out.println(claims);

        Long expiryTime = (Long) claims.get("exp");
        Long currentTime = System.currentTimeMillis();

        if (expiryTime < currentTime) {
            //Token is InValid.

            //TODO - Check expiry Time and current time (Milliseconds vs Seconds) issue.
            System.out.println("Expiry time : " + expiryTime);
            System.out.println("Current time : " + currentTime);

            throw new RuntimeException("Invalid JWT token.");
        }
        //Token is Valid.
        Long userId = (Long) claims.get("userId");
        Optional<User> optionalUser = userRepository.findById(userId);

        return optionalUser.get();
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
