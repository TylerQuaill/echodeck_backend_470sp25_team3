// This controller handles all soundboard actions such as viewing the sound library, adding/removing sounds, and enforcing sound limits

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

    // Get the current authenticated user
    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Public Endpoint: Return all sounds in the soundboard library
    @GetMapping("/library")
    public List<Sound> getAllSounds() {
        return soundRepo.findAll();
    }

    // Authenticated Endpoint: Return the sounds on the user's board
    @GetMapping("/my-sounds")
    public List<Sound> getUserActiveSounds() {
        User user = getAuthenticatedUser();
        return userSoundRepo.findByUserAndIsActive(user, true)
                .stream()
                .map(UserSoundboard::getSound)
                .collect(Collectors.toList());
    }

    // Authenticated Endpoint: Add a sound to user's  board with a role based limit 
    @PostMapping("/add/{soundId}")
    public ResponseEntity<?> addSound(@PathVariable Long soundId) {
        User user = getAuthenticatedUser();
        Sound sound = soundRepo.findById(soundId).orElseThrow();
        
        // Count how many active sounds the user has already
        int activeCount = userSoundRepo.findByUserAndIsActive(user, true).size();
         
        // Set the sound limit based on role
        String role = user.getRole();
        int limit = 5;
        
        if (role != null && (role.equalsIgnoreCase("USER")
                || role.equalsIgnoreCase("PRIVILEGED_USER")
                || role.equalsIgnoreCase("ADMIN"))) {
            limit = 20;
        }
        // If the limit is reached, return this error
        if (activeCount >= limit) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Sound limit reached for your account (" + limit + " sounds max).");
        }
        
        // Add or activate the sound for the user
        UserSoundboard entry = userSoundRepo.findByUserAndSound(user, sound)
                .orElse(new UserSoundboard());

        entry.setUser(user);
        entry.setSound(sound);
        entry.setIsActive(true);
        userSoundRepo.save(entry);

        return ResponseEntity.ok("Sound added to user's board");
    }

    // Authenticated Endpoint: Remove a sound from the user's board (soft delete)
    @DeleteMapping("/remove/{soundId}")
    public ResponseEntity<?> removeSound(@PathVariable Long soundId) {
        User user = getAuthenticatedUser();
        Sound sound = soundRepo.findById(soundId).orElseThrow();

        UserSoundboard entry = userSoundRepo.findByUserAndSound(user, sound)
                .orElseThrow();

        entry.setIsActive(false); // mark as inactive
        userSoundRepo.save(entry);
        return ResponseEntity.ok("Sound removed from user's board");
    }
}
