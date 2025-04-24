// This service provides access to sound related data such as retrieving all sounds in the library.

package edu.uscb.csci470sp25_team3.echodeck_backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.SoundRepository;

@Service
public class SoundService {
    @Autowired
    private SoundRepository soundRepository;
    
    // Return all sounds from the database
    public List<Sound> getAllSounds() {
        return soundRepository.findAll();
    }
}