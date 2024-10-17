package com.rose.online_learning_platform.auth.services;


import com.rose.online_learning_platform.auth.dto.CreateUpdateUserDto;
import com.rose.online_learning_platform.auth.dto.UserDTO;
import com.rose.online_learning_platform.auth.entities.Role;
import com.rose.online_learning_platform.auth.entities.User;
import com.rose.online_learning_platform.auth.repository.RoleRepository;
import com.rose.online_learning_platform.auth.repository.UserRepository;
import com.rose.online_learning_platform.commons.enums.ResponseStatusEnum;
import com.rose.online_learning_platform.commons.enums.RolesEnum;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.responses.GenericResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public final UserDTO convertToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFullName(user.getFullName());
        userDTO.setEmail(user.getEmail());
        userDTO.setIsActive(true);
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    public final User convertToEntity(CreateUpdateUserDto userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return user;
    }

    @Cacheable(value = "users")
    public Iterable<User> findAll(int page, int size) {
        return userRepository.findAll(
                PageRequest.of(page, size)
        );

    }

    @Cacheable(value = "users_all")
    public Iterable<UserDTO> findAll() {
        return userRepository.findAll().stream().map(this::convertToDto).toList();
    }

    public UserDTO create(CreateUpdateUserDto userDTO) {
        User user = convertToEntity(userDTO);
        user = userRepository.save(user);
        return convertToDto(user);
    }


    public User createApprover(CreateUpdateUserDto createUpdateUserDto) throws EMException {
        Optional<User> existingUserByEmail = userRepository.findByEmail(createUpdateUserDto.getEmail());


        Optional<Role> optionalRole = roleRepository.findByName(RolesEnum.APPROVER);

        if (optionalRole.isEmpty()) {
            throw EMException.builder().message("Role not found").build();
        }

        User newUser = User.builder()
                .email(createUpdateUserDto.getEmail())
                .fullName(createUpdateUserDto.getFullName())
                .password(passwordEncoder.encode(createUpdateUserDto.getPassword()))
                .roles(Set.of(optionalRole.get()))
                .acceptTerms(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isActive(true)
                .build();

        userRepository.save(newUser);

        return newUser;
    }



    @Cacheable(value = "users", key = "#id")
    public User findById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        return user;
    }

    public GenericResponse<User> addRolesToUser(String userId, Set<Long> roleIds) throws EMException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> EMException.builder().message("User not found").build());

        List<Role> rolesToAdd = new ArrayList<>();
        for (Long roleId : roleIds) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> EMException.builder().message("Role not found").build());
            if (!user.getRoles().contains(role)) {
                rolesToAdd.add(role);
            }
        }

        user.getRoles().addAll(rolesToAdd);

        var savedUserRole = userRepository.save(user);
        return GenericResponse.<User>builder()
                .status(ResponseStatusEnum.SUCCESS)
                .data(savedUserRole)
                .debugMessage("role assigned")
                .message("User added a Role successfully")
                .build();
    }

    public GenericResponse<User> removeRolesFromUser(String userId, Set<Long> roleIds) throws EMException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> EMException.builder().message("No user record").build());

        for (Long roleId : roleIds) {
            Role roleToRemove = roleRepository.findById(roleId)
                    .orElseThrow(() -> EMException.builder().message("Role not found").build());

            if (user.getRoles().contains(roleToRemove)) {
                user.getRoles().remove(roleToRemove);
            }
        }

        var userRemovedRole=  userRepository.save(user);

        return GenericResponse.<User>builder()
                .status(ResponseStatusEnum.SUCCESS)
                .data(userRemovedRole)
                .debugMessage("role revoked from user")
                .message("User removed Role successfully")
                .build();
    }

    public UserDTO update(String id, CreateUpdateUserDto userDTO) {
        var user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        user = convertToEntity(userDTO);
        user = userRepository.save(user);
        return convertToDto(user);
    }

    public void delete(String id) {
        var user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepository.delete(user);
    }

}
