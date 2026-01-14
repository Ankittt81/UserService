package com.smartcart.userservice.services;

import com.smartcart.userservice.exceptions.PasswordMisMatchException;
import com.smartcart.userservice.models.Token;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder  bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public User signup(String name, String email, String password) {
        Optional<User> userOptional=userRepository.findByEmail(email);
        if(userOptional.isPresent()){
            //Redirect to login
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

        return null;
    }

    @Override
    public User validateToken(String tokenValue) {
        return null;
    }

    @Override
    public User logout(String email) {
        return null;
    }
}
