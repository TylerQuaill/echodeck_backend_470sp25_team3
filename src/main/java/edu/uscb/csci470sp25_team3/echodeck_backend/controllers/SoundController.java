package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/api/sounds")
    public List<Sound> getSounds() {
        return soundRepository.findAll(); // Fetch all sounds from the database
    }
}
