package edu.uscb.csci470sp25_team3.echodeck_backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;
import edu.uscb.csci470sp25_team3.echodeck_backend.services.SoundService;

@RestController
@RequestMapping("/api/sounds")
public class SoundController {
    @Autowired
    private SoundService soundService;
    @GetMapping
    public ResponseEntity<List<Sound>> getSounds() {
        return ResponseEntity.ok(soundService.getAllSounds());
    }
}
