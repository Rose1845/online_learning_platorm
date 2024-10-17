package com.rose.online_learning_platform.auth.repository;


import com.rose.online_learning_platform.auth.entities.Role;
import com.rose.online_learning_platform.commons.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RolesEnum name);
}
