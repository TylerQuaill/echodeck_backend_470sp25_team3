// This controller handles user authentication, registration, and guest account creation for the app.

package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserRepository;
import edu.uscb.csci470sp25_team3.echodeck_backend.security.JwtUtil;
import edu.uscb.csci470sp25_team3.echodeck_backend.services.AuthService;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;




    // Register a new user
    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {
        return authService.registerUser(authRequest.getEmail(), authRequest.getPassword(), authRequest.getRole());
    }

    // Login a user and return JWT token
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        return authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());
    }

    // Create a guest user using the service and returns a token 
    @PostMapping("/guest")
    public ResponseEntity<String> createGuestUser() {
    	// Generate email and password for guest
        String guestEmail = "guest_" + UUID.randomUUID() + "@echodeck.local";
        String guestPassword = UUID.randomUUID().toString(); 
        String encodedPassword = passwordEncoder.encode(guestPassword);
        
        // Create a new guest user with role "GUEST"
        User guest = new User();
        guest.setEmail(guestEmail);
        guest.setPassword(encodedPassword);
        guest.setRole("GUEST");
        
        // Save the guest to the db
        userRepository.save(guest);
        
        // Generate and return a token for the guest
        String token = jwtUtil.generateToken(guest);
        return ResponseEntity.ok(token); 
    }
}
