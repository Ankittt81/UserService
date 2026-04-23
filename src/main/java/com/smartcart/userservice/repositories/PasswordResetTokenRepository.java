package com.smartcart.userservice.repositories;

import com.smartcart.userservice.models.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    @Override
     PasswordResetToken save(PasswordResetToken passwordResetToken);

    Optional<PasswordResetToken> findByEmail(String email);
    void deleteByEmail(String email);
}
