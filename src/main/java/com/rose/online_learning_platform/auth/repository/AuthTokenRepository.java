package com.rose.online_learning_platform.auth.repository;


import com.rose.online_learning_platform.auth.entities.AuthToken;
import com.rose.online_learning_platform.auth.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthTokenRepository extends CrudRepository<AuthToken, Long> {
    Optional<AuthToken> findByToken(String token);
    Optional <AuthToken> findByUserId(User userId);
}
