package edu.uscb.csci470sp25_team3.echodeck_backend.repository;

import edu.uscb.csci470sp25_team3.echodeck_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}