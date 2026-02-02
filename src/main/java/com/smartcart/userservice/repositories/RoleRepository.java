package com.smartcart.userservice.repositories;

import com.smartcart.userservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Long, Role> {
    boolean existsByName(String name);
    Optional<Role> findByName(String name);
    Role save(Role role);
}
