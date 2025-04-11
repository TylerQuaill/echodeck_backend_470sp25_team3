package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    // Get all the sounds for library the guest and signed in users
    @GetMapping("/library")
    public List<Sound> getAllSounds() {
        return soundRepo.findAll();
    }

    // Get the active sounds for a signed-in user
    @GetMapping("/my-sounds/{userId}")
    public List<Sound> getUserActiveSounds(@PathVariable Long userId) {
        User user = userRepo.findById(userId).orElseThrow();
        return userSoundRepo.findByUserAndIsActive(user, true)
                .stream()
                .map(UserSoundboard::getSound)
                .collect(Collectors.toList());
    }

    // Add a sound to users board
    @PostMapping("/add/{userId}/{soundId}")
    public ResponseEntity<?> addSound(@PathVariable Long userId, @PathVariable Long soundId) {
        User user = userRepo.findById(userId).orElseThrow();
        Sound sound = soundRepo.findById(soundId).orElseThrow();
        UserSoundboard entry = userSoundRepo.findByUserAndSound(user, sound)
                .orElse(new UserSoundboard());
        entry.setUser(user);
        entry.setSound(sound);
        entry.setIsActive(true);
        userSoundRepo.save(entry);
        return ResponseEntity.ok("Sound added to user's board");
    }

    // Remove sound from users board
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
