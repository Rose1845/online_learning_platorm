package com.rose.online_learning_platform.auth.services;


import com.rose.online_learning_platform.auth.dto.RoleDTO;
import com.rose.online_learning_platform.auth.entities.Role;
import com.rose.online_learning_platform.auth.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public RoleDTO createRole (RoleDTO role) {
        Role newRole = new Role();
        newRole.setName(role.getName());
        newRole.setDescription(role.getDescription());
        newRole.setIsDeleted(role.getIsDeleted());
        try {
            Role savedRole = roleRepository.save(newRole);
            role.setId(savedRole.getId());
            role.setIsDeleted(savedRole.getIsDeleted());
            role.setCreatedAt(savedRole.getCreatedAt());
            role.setUpdatedAt(savedRole.getUpdatedAt());
            return role;
        } catch (Exception e) {
            throw new RuntimeException("Error creating role");
        }
    }

    @Cacheable(value = "roles")
    public Iterable<RoleDTO> getRoles() {
       Iterable<Role> roles = roleRepository.findAll();
       return StreamSupport.stream(roles.spliterator(), false)
               .map(role -> {
                   RoleDTO roleDTO = new RoleDTO();
                   roleDTO.setId(role.getId());
                   roleDTO.setName(role.getName());
                   roleDTO.setIsDeleted(role.getIsDeleted());
                   roleDTO.setDescription(role.getDescription());
                   roleDTO.setCreatedAt(role.getCreatedAt());
                   roleDTO.setUpdatedAt(role.getUpdatedAt());
                   return roleDTO;
               }).toList();
    }

    @Cacheable(value = "roles", key = "#id")
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        roleDTO.setIsDeleted(role.getIsDeleted());
        roleDTO.setDescription(role.getDescription());
        roleDTO.setCreatedAt(role.getCreatedAt());
        roleDTO.setUpdatedAt(role.getUpdatedAt());
        return roleDTO;
    }

    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO role) {
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        existingRole.setName(role.getName());
        existingRole.setDescription(role.getDescription());
        existingRole.setIsDeleted(role.getIsDeleted());
        try {
            Role updatedRole = roleRepository.save(existingRole);
            role.setId(updatedRole.getId());
            role.setIsDeleted(role.getIsDeleted());
            role.setCreatedAt(updatedRole.getCreatedAt());
            role.setUpdatedAt(updatedRole.getUpdatedAt());
            return role;
        } catch (Exception e) {
            throw new RuntimeException("Error updating role");
        }
    }

    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));
        roleRepository.delete(role);
    }
}
