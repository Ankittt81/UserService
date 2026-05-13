package com.smartcart.userservice.repositories;

import com.smartcart.userservice.models.Address;
import com.smartcart.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address,Long> {
    long countByUser(User user);
    @Override
    <S extends Address> S save(S entity);
    List<Address> findByUser(User user);
    @Modifying
    @Query("""
    update Address a
    set a.isDefault=false
     where a.user.id=:userId
     """)
    void clearDefaultForUser(Long userId);
    boolean existsByUser(User user);
}
