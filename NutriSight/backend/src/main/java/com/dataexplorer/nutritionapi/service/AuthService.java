package com.dataexplorer.nutritionapi.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dataexplorer.nutritionapi.model.User;
import com.dataexplorer.nutritionapi.repository.UserRepository;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Register a new user
     * @param email user's email
     * @param password plain text password
     * @param firstName user's first name
     * @param lastName user's last name
     * @return created User or null if email exists
     */
    public User register(String email, String password, String firstName, String lastName) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            return null; // Email already taken
        }
        
        // Create new user with hashed password
        User user = new User(email, hashPassword(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        
        return userRepository.save(user);
    }
    
    /**
     * Login user
     * @param email user's email
     * @param password plain text password
     * @return User if credentials valid, null otherwise
     */
    public User login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            return null; // User not found
        }
        
        User user = userOptional.get();
        String hashedPassword = hashPassword(password);
        
        // Check if password matches
        if (user.getPassword().equals(hashedPassword)) {
            return user;
        }
        
        return null; // Invalid password
    }
    
    /**
     * Simple password hashing using SHA-256
     * NOTE: In production, use BCrypt or similar
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}