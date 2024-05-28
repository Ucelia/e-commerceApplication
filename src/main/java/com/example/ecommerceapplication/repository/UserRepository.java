package com.example.ecommerceapplication.repository;

import com.example.ecommerceapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    static List<User> findById(Long id) {
        return null;
    }

    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
}
