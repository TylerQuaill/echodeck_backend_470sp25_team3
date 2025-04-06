package edu.uscb.csci470sp25_team3.echodeck_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional; 

import edu.uscb.csci470sp25_team3.echodeck_backend.model.User; 
import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;
import edu.uscb.csci470sp25_team3.echodeck_backend.entities.UserSoundboard;

public interface UserSoundboardRepository extends JpaRepository<UserSoundboard, Long> {
    List<UserSoundboard> findByUserAndIsActive(User user, boolean isActive);
    Optional<UserSoundboard> findByUserAndSound(User user, Sound sound);
}
