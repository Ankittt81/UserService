package com.smartcart.userservice.repositories;

import com.smartcart.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,Long> {
    @Override
    Token save(Token token);
}
