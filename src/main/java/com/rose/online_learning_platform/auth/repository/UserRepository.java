package com.rose.online_learning_platform.auth.repository;

import com.rose.online_learning_platform.auth.entities.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(@Email(message = "Email should be valid") String email);
    Boolean existsUserByEmail(@Email(message = "Email should be valid") String email);
}

