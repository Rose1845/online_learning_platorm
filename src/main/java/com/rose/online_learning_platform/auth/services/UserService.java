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


    public UserDTO update(String id, CreateUpdateUserDto userDTO) {
        var user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getEmail()));
        user = userRepository.save(user);
        return convertToDto(user);
    }

    public void delete(String id) {
        var user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        userRepository.delete(user);
    }

}
