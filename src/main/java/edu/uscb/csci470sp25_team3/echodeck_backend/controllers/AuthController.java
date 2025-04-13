package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import edu.uscb.csci470sp25_team3.echodeck_backend.services.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest) {
        return authService.registerUser(authRequest.getEmail(), authRequest.getPassword(), authRequest.getRole());
    }

    // ✅ Login user
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        return authService.authenticateUser(authRequest.getEmail(), authRequest.getPassword());
    }
}
