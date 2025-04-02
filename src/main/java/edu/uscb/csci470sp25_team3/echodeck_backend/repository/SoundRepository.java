package edu.uscb.csci470sp25_team3.echodeck_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.uscb.csci470sp25_team3.echodeck_backend.entities.Sound;

@Repository
public interface SoundRepository extends JpaRepository<Sound, Long> {
}