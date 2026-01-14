package com.smartcart.userservice.repositories;

import com.smartcart.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {
    @Override
    Token save(Token token);

    Token findByTokenValue(String tokenValue);
    Token findByExpiryAtGreaterThan(Date time);

    Optional<Token> findTokensByTokenValueAndExpiryAtGreaterThan(String tokenValue, Date time);
}
