// What this test does is check if the frontend or Postman can talk to the backend using real API routes
package edu.uscb.csci470sp25_team3.echodeck_backend.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;
import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.SoundRepository;
import edu.uscb.csci470sp25_team3.echodeck_backend.repository.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SoundboardControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(SoundboardControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SoundRepository soundRepository;

    private Long testUserId;
    private Long testSoundId;

    @BeforeEach
    public void setup() {
        // Arrange: Creates user and sound
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole("USER");
        userRepository.save(user);
        testUserId = user.getId();

        Sound sound = new Sound();
        sound.setName("Test Sound");
        sound.setFileUrl("https://example.com/test.mp3");
        sound.setArtist("Test Artist");
        sound.setCredit("Test Credit");
        soundRepository.save(sound);
        testSoundId = sound.getId();
    }

    @Test
    public void testGetSoundLibrary() throws Exception {
        // Act and Assert: Makes sure endpoint returns a JSON list
        mockMvc.perform(get("/api/soundboard/library"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAddSoundToUserBoard() throws Exception {
        // Act & Assert: Adds a sound to the users board and expects a confirmation message
        mockMvc.perform(post("/api/soundboard/add/" + testUserId + "/" + testSoundId))
                .andExpect(status().isOk())
                .andExpect(content().string("Sound added to user's board"));
    }

    @Test
    public void testRemoveSoundFromUserBoard() throws Exception {
        // Arrange: First adds the sound so it can be removed
        mockMvc.perform(post("/api/soundboard/add/" + testUserId + "/" + testSoundId))
                .andExpect(status().isOk());

        // Act & Assert: Removes the sound and gives confirmation message
        mockMvc.perform(delete("/api/soundboard/remove/" + testUserId + "/" + testSoundId))
                .andExpect(status().isOk())
                .andExpect(content().string("Sound removed from user's board"));
    }
}
