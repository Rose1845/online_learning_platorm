package com.rose.online_learning_platform.auth.controllers;

import com.rose.online_learning_platform.auth.dto.CreateUpdateUserDto;
import com.rose.online_learning_platform.auth.dto.UserDTO;
import com.rose.online_learning_platform.auth.entities.User;
import com.rose.online_learning_platform.auth.services.UserService;
import com.rose.online_learning_platform.commons.enums.ResponseStatusEnum;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.responses.GenericResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Iterable<?>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> create(@Valid @RequestBody CreateUpdateUserDto userDto) {
        return ResponseEntity.ok(userService.create(userDto));
    }

    @PostMapping(value = "/approver", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createApprover(@Valid @RequestBody CreateUpdateUserDto userDto) throws EMException {
        return ResponseEntity.ok(userService.createApprover(userDto));
    }

    @PostMapping(value="/add-role-to-user/{userId}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<User>> addUserToRole(@Valid @RequestBody Set<Long> useRoles, @PathVariable  String userId) throws EMException {

        try{
            return ResponseEntity.ok(userService.addRolesToUser(userId,useRoles));

        }catch (EMException e){
            GenericResponse<User> errorResponse = GenericResponse.<User>builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(ResponseStatusEnum.ERROR)
                    .debugMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch (Exception e) {
            GenericResponse<User> errorResponse = GenericResponse.<User>builder()
                    .data(null)
                    .message("An unexpected error occurred")
                    .status(ResponseStatusEnum.ERROR)
                    .debugMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }
    }

    @PostMapping(value="/revoke-role-to-user/{userId}",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<User>> revokeRoleToUser(@Valid @RequestBody Set<Long> useRoles, @PathVariable  String userId) throws EMException {

        try{
            return ResponseEntity.ok(userService.removeRolesFromUser(userId,useRoles));

        }catch (EMException e){
            GenericResponse<User> errorResponse = GenericResponse.<User>builder()
                    .data(null)
                    .message(e.getMessage())
                    .status(ResponseStatusEnum.ERROR)
                    .debugMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        catch (Exception e) {
            GenericResponse<User> errorResponse = GenericResponse.<User>builder()
                    .data(null)
                    .message("An unexpected error occurred")
                    .status(ResponseStatusEnum.ERROR)
                    .debugMessage(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> update(@PathVariable String id, @Valid @RequestBody CreateUpdateUserDto userDto) {
        return ResponseEntity.ok(userService.update(id, userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
