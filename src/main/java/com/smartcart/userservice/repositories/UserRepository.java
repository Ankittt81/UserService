package com.smartcart.userservice.repositories;

import com.smartcart.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);


  //  User save(String name,String email, String password);

    User save(User user);
}
