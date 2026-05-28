package com.smartcart.userservice.config;

import com.smartcart.userservice.models.Role;
import com.smartcart.userservice.models.Status;
import com.smartcart.userservice.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RoleInitializer {
    private final RoleRepository roleRepository;
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        createRoleIfNotExists("ROLE_USER");
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_SELLER");

    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            role.setStatus(Status.ACTIVE);
            roleRepository.save(role);

            log.info("Created role: {}", roleName);
        }
    }
}
