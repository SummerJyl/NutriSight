package com.dataexplorer.nutritionapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dataexplorer.nutritionapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by email (for login)
    Optional<User> findByEmail(String email);
    
    // Check if email already exists (for registration validation)
    boolean existsByEmail(String email);
}