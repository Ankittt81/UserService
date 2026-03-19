package com.smartcart.userservice.services;

import com.smartcart.userservice.events.ResetPasswordEvent;
import com.smartcart.userservice.exceptions.UserNotFoundException;
import com.smartcart.userservice.mappers.OtpMapper;
import com.smartcart.userservice.models.PasswordResetToken;
import com.smartcart.userservice.models.User;
import com.smartcart.userservice.repositories.PasswordResetTokenRepository;
import com.smartcart.userservice.repositories.UserRepository;
import com.smartcart.userservice.util.OtpUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {
    private PasswordResetTokenRepository passwordResetTokenRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private OtpMapper  otpMapper;


    public OtpService(PasswordResetTokenRepository passwordResetTokenRepository,
                      BCryptPasswordEncoder bCryptPasswordEncoder, OtpMapper otpMapper
                      ) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.otpMapper = otpMapper;

    }

    @Transactional
    public String generateAndSaveOtp(String email){

        Optional<PasswordResetToken> existingOptional=passwordResetTokenRepository.findByEmail(email);

        if(!existingOptional.isEmpty()){
            PasswordResetToken existing=existingOptional.get();
            LocalDateTime lastRequestTime=existing.getCreatedAt();
            if(lastRequestTime.plusSeconds(30).isAfter(LocalDateTime.now())){
                throw new RuntimeException("Please wait before requesting OTP again");
            }
        }
        String otp= OtpUtil.generateOtp(6);
        String hashOtp=bCryptPasswordEncoder.encode(otp);
        PasswordResetToken passwordResetToken= otpMapper.toEntity(email,hashOtp);
        passwordResetTokenRepository.save(passwordResetToken);


        return otp;
    }

    @Transactional
    public boolean validateOtp(String email,String otp){
        Optional<PasswordResetToken> tokenOptional=passwordResetTokenRepository.findByEmail(email);

        if(tokenOptional.isEmpty()){
            throw new RuntimeException("No OTP request found");
        }
        PasswordResetToken token=tokenOptional.get();
        // 1. Check expiry
        if(token.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP expired");
        }

        // 2. Check attempts
        if(token.getAttempts()>=3){
            throw new RuntimeException("Too many attempts");
        }

        // 3. Verify OTP
        if(!bCryptPasswordEncoder.matches(otp,token.getHashOtp())){
            token.setAttempts(token.getAttempts()+1);
            passwordResetTokenRepository.save(token);
            throw new RuntimeException("Invalid OTP");
        }
        // 4. Mark verified
        token.setVerified(true);
        passwordResetTokenRepository.save(token);
        return true;
    }

    public void verifyOtp(String email){
        Optional<PasswordResetToken> tokenOptional=passwordResetTokenRepository.findByEmail(email);
        if(tokenOptional.isEmpty()){
            throw new RuntimeException("No request found");
        }
        PasswordResetToken token=tokenOptional.get();
        if(token.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP expired");
        }

        // 2. Check attempts
        if(token.getAttempts()>=3){
            throw new RuntimeException("Too many attempts");
        }
        // ❗ IMPORTANT CHECK
        if(!token.isVerified()){
            throw new RuntimeException("OTP not verified");
        }

        //clean-up
        passwordResetTokenRepository.delete(token);
    }
}
