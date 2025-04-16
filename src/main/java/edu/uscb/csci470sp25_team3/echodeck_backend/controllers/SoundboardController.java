package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;
import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;
import edu.uscb.csci470sp25_team3.echodeck_backend.entities.UserSoundboard;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.SoundRepository;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserSoundboardRepository;

@RestController
@RequestMapping("/api/soundboard")
public class SoundboardController {

    @Autowired
    private SoundRepository soundRepo;

    @Autowired
    private UserSoundboardRepository userSoundRepo;

    // Utility to get the current authenticated user
    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Public: Get all sounds
    @GetMapping("/library")
    public List<Sound> getAllSounds() {
        return soundRepo.findAll();
    }

    // Authenticated: Get sounds on the user's board
    @GetMapping("/my-sounds")
    public List<Sound> getUserActiveSounds() {
        User user = getAuthenticatedUser();
        return userSoundRepo.findByUserAndIsActive(user, true)
                .stream()
                .map(UserSoundboard::getSound)
                .collect(Collectors.toList());
    }

    // Authenticated: Add a sound to user board
    @PostMapping("/add/{soundId}")
    public ResponseEntity<?> addSound(@PathVariable Long soundId) {
        User user = getAuthenticatedUser();
        Sound sound = soundRepo.findById(soundId).orElseThrow();

        int activeCount = userSoundRepo.findByUserAndIsActive(user, true).size();

        String role = user.getRole();
        int limit = 5;
        if (role != null && (role.equalsIgnoreCase("USER")
                || role.equalsIgnoreCase("PRIVILEGED_USER")
                || role.equalsIgnoreCase("ADMIN"))) {
            limit = 20;
        }

        if (activeCount >= limit) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sound limit reached for your account (" + limit + " sounds max).");
        }

        UserSoundboard entry = userSoundRepo.findByUserAndSound(user, sound)
                .orElse(new UserSoundboard());

        entry.setUser(user);
        entry.setSound(sound);
        entry.setIsActive(true);
        userSoundRepo.save(entry);

        return ResponseEntity.ok("Sound added to user's board");
    }

    // Authenticated: Remove a sound
    @DeleteMapping("/remove/{soundId}")
    public ResponseEntity<?> removeSound(@PathVariable Long soundId) {
        User user = getAuthenticatedUser();
        Sound sound = soundRepo.findById(soundId).orElseThrow();

        UserSoundboard entry = userSoundRepo.findByUserAndSound(user, sound)
                .orElseThrow();

        entry.setIsActive(false);
        userSoundRepo.save(entry);
        return ResponseEntity.ok("Sound removed from user's board");
    }
}
