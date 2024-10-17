package com.rose.online_learning_platform.auth.repository;

import com.rose.online_learning_platform.auth.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
