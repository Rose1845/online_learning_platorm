package com.rose.online_learning_platform.instructor.service;


import com.rose.online_learning_platform.auth.dto.CreateUpdateUserDto;
import com.rose.online_learning_platform.auth.entities.Role;
import com.rose.online_learning_platform.auth.entities.User;
import com.rose.online_learning_platform.auth.repository.RoleRepository;
import com.rose.online_learning_platform.auth.repository.UserRepository;
import com.rose.online_learning_platform.commons.enums.RolesEnum;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.instructor.entity.Instructor;
import com.rose.online_learning_platform.instructor.repository.InstructorRepository;
import com.rose.online_learning_platform.instructor.response.InstructorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InstructorService {
    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public InstructorResponse createInstructor(CreateUpdateUserDto createUpdateUserDto) throws EMException {
        Optional<User> existingUserByEmail = userRepository.findByEmail(createUpdateUserDto.getEmail());

        Instructor instructor = new Instructor();
        instructor.setEmail(createUpdateUserDto.getEmail());
        instructor.setFullName(createUpdateUserDto.getFullName());

        Optional<Role> optionalRole = roleRepository.findByName(RolesEnum.INSTRUCTOR);

        if (optionalRole.isEmpty()) {
            throw EMException.builder().message("Role not found").build();
        }
        if (existingUserByEmail.isPresent()) {
            User existingUser = existingUserByEmail.get();
            instructor.setUser(existingUser);
        } else {
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
            instructor.setUser(newUser);
        }

        Instructor savedInstructor = instructorRepository.save(instructor);

        return InstructorResponse.builder()
                .fullName(savedInstructor.getFullName())
                .email(savedInstructor.getEmail())
                .build();
    }



    public List<Instructor> getAllTeachers() {
        return instructorRepository.findAll();
    }

    public Optional<Instructor> getTeacherById(Long teacherId) {
        return instructorRepository.findById(teacherId);

    }

    public String deleteTeacher(Long teacherId) {
        instructorRepository.deleteById(teacherId);
        return "Instructor with " + teacherId + "has been deleted";
    }


}
