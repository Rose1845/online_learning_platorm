package com.rose.online_learning_platform.auth.repository;

import com.rose.online_learning_platform.auth.entities.AuthProvider;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthProviderRepository extends CrudRepository<AuthProvider, Long> {
}
