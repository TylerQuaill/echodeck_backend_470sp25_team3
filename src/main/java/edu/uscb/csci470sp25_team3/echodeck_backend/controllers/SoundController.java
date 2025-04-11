package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.SoundRepository;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from your frontend
public class SoundController {

    private final SoundRepository soundRepository;

    public SoundController(SoundRepository soundRepository) {
        this.soundRepository = soundRepository;
    }

    // Fetch all sounds
    @GetMapping("/api/sounds")
    public List<Sound> getSounds() {
        return soundRepository.findAll(); // Fetch all sounds from the database
    }

    // Fetch a specific sound by ID
    @GetMapping("/api/sounds/{id}")
    public ResponseEntity<Sound> getSoundById(@PathVariable Long id) {
        Optional<Sound> sound = soundRepository.findById(id);
        if (sound.isPresent()) {
            return ResponseEntity.ok(sound.get()); // Return the sound if found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 if not found
        }
    }
}
