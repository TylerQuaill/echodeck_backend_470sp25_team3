package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/register")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}