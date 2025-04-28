// This service handles user registration, login, and guest account creation

package edu.uscb.csci470sp25_team3.echodeck_backend.services;

import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserRepository;
import edu.uscb.csci470sp25_team3.echodeck_backend.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registers a new user by encoding their password and saving them to the database.
     */
    public String registerUser(String email, String password, String role) {
        // ✅ Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists.");
        }

        // Create and save the user
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(email, hashedPassword, role);
        userRepository.save(newUser);

        return "User registered successfully";
    }

    /**
     * Authenticates a user by verifying their email and password, then generating a JWT token.
     */
    public String authenticateUser(String email, String password) {
        // ✅ Find user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid email or password.");
        }

        User user = userOptional.get();

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password.");
        }

        // Return JWT token
        return jwtUtil.generateToken(user);
    }
    
    // Generates temp guest user accoount with a random email and default "GUEST" role
    public User createGuestUser() {
        String guestEmail = "guest_" + UUID.randomUUID() + "@echodeck.local";
        User guest = new User();
        guest.setEmail(guestEmail);
        guest.setPassword(passwordEncoder.encode("guest")); // Encoded
        guest.setRole("GUEST");
        return userRepository.save(guest);
    }

}
