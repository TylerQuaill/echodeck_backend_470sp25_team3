package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;
import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;
import edu.uscb.csci470sp25_team3.echodeck_backend.entities.UserSoundboard;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.SoundRepository;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserRepository;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserSoundboardRepository;

@RestController
@RequestMapping("/api/soundboard")
public class SoundboardController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private SoundRepository soundRepo;

    @Autowired
    private UserSoundboardRepository userSoundRepo;

    // Get all the sounds in the system both guest and signed in
    @GetMapping("/library")
    public List<Sound> getAllSounds() {
        return soundRepo.findAll();
    }

    // Get all active sounds for a signed-in user
    @GetMapping("/my-sounds/{userId}")
    public List<Sound> getUserActiveSounds(@PathVariable Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return userSoundRepo.findByUserAndIsActive(user, true)
                .stream()
                .map(UserSoundboard::getSound)
                .collect(Collectors.toList());
    }

    // Add a sound to a user's board, with role-based limits (5 for guest 20 for signed-in)
    @PostMapping("/add/{userId}/{soundId}")
    public ResponseEntity<?> addSound(@PathVariable Long userId, @PathVariable Long soundId) {
        User user = userRepo.findById(userId).orElseThrow();
        Sound sound = soundRepo.findById(soundId).orElseThrow();

        // Count how many active sounds the user already has
        int activeCount = userSoundRepo.findByUserAndIsActive(user, true).size();

        String role = user.getRole();
        int limit = 5;

        if (role != null && (role.equalsIgnoreCase("USER") || role.equalsIgnoreCase("PRIVILEGED_USER") || role.equalsIgnoreCase("ADMIN"))) {
            limit = 20;
        }


        if (activeCount >= limit) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("Sound limit reached for your account (" + limit + " sounds max).");
        }

        // Add or activate the sound again
        UserSoundboard entry = userSoundRepo.findByUserAndSound(user, sound)
                .orElse(new UserSoundboard());

        entry.setUser(user);
        entry.setSound(sound);
        entry.setIsActive(true);
        userSoundRepo.save(entry);

        return ResponseEntity.ok("Sound added to user's board");
    }

    // Remove a sound from a user's board inactive not delete
    @DeleteMapping("/remove/{userId}/{soundId}")
    public ResponseEntity<?> removeSound(@PathVariable Long userId, @PathVariable Long soundId) {
        User user = userRepo.findById(userId).orElseThrow();
        Sound sound = soundRepo.findById(soundId).orElseThrow();
        UserSoundboard entry = userSoundRepo.findByUserAndSound(user, sound)
                .orElseThrow();
        entry.setIsActive(false);
        userSoundRepo.save(entry);
        return ResponseEntity.ok("Sound removed from user's board");
    }
}
