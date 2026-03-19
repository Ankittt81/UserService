package com.smartcart.userservice.mappers;

import com.smartcart.userservice.models.PasswordResetToken;
import com.smartcart.userservice.repositories.PasswordResetTokenRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Getter
@Setter
@Component
public class OtpMapper {
    public PasswordResetToken toEntity(String email,String hashOtp){
        PasswordResetToken passwordResetToken=new PasswordResetToken();
        passwordResetToken.setEmail(email);
        passwordResetToken.setHashOtp(hashOtp);
        passwordResetToken.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        passwordResetToken.setAttempts(0);
        passwordResetToken.setVerified(false);
        return passwordResetToken;
    }
}
