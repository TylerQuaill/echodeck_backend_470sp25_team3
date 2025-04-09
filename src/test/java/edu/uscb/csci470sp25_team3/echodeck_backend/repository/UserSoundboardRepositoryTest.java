//What this test does is check if we can save and find soundboard data for each user
package edu.uscb.csci470sp25_team3.echodeck_backend.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;
import edu.uscb.csci470sp25_team3.echodeck_backend.entities.UserSoundboard;
import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserSoundboardRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SoundRepository soundRepository;

    @Autowired
    private UserSoundboardRepository userSoundboardRepository;

    @Test
    public void testSaveAndFindActiveSound() {
        // Arrange: Creates a user and sound and saves it
        User user = new User();
        user.setEmail("user1@example.com");
        user.setPassword("password");
        user.setRole("USER");
        userRepository.save(user);

        Sound sound = new Sound();
        sound.setName("Airhorn");
        sound.setFileUrl("https://example.com/airhorn.mp3");
        sound.setArtist("Anonymous");
        sound.setCredit("FreeSound");
        soundRepository.save(sound);

        UserSoundboard usb = new UserSoundboard();
        usb.setUser(user);
        usb.setSound(sound);
        usb.setIsActive(true);
        userSoundboardRepository.save(usb);

        // Act: Finds active sounds for the user
        List<UserSoundboard> active = userSoundboardRepository.findByUserAndIsActive(user, true);

        // Assert: The sound should be found and match what was saved
        assertEquals(1, active.size());
        assertEquals("Airhorn", active.get(0).getSound().getName());
    }

    @Test
    public void testDeactivateSound() {
        // Arrange: Creates a user and sound and activates it
        User user = new User();
        user.setEmail("user2@example.com");
        user.setPassword("password");
        user.setRole("USER");
        userRepository.save(user);

        Sound sound = new Sound();
        sound.setName("Clap");
        sound.setFileUrl("https://example.com/clap.mp3");
        sound.setArtist("Test Artist");
        sound.setCredit("Creative Commons");
        soundRepository.save(sound);

        UserSoundboard usb = new UserSoundboard();
        usb.setUser(user);
        usb.setSound(sound);
        usb.setIsActive(true);
        userSoundboardRepository.save(usb);

        // Act: Deactivates the sound
        usb.setIsActive(false);
        userSoundboardRepository.save(usb);
        List<UserSoundboard> active = userSoundboardRepository.findByUserAndIsActive(user, true);

        // Assert: No active sounds should be found
        assertTrue(active.isEmpty(), "Sound should not be active after deactivation.");
    }
}
