package com.rose.online_learning_platform.auth.controllers;

import com.rose.online_learning_platform.auth.dto.RoleDTO;
import com.rose.online_learning_platform.auth.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin/roles")
@Tag(name = "Role Management", description = "CRUD operations for roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> registerUser (@Valid @RequestBody RoleDTO req) {
        return ResponseEntity.ok(roleService.createRole(req));
    }

    @Operation(summary = "Get role by ID")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    @Operation(summary = "Get all roles")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @Operation(summary = "Update an existing role")
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO req) {
        return ResponseEntity.ok(roleService.updateRole(id, req));
    }

    @Operation(summary = "Delete a role")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
