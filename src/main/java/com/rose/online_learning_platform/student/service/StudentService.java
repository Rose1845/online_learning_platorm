package com.rose.online_learning_platform.student.service;

import com.rose.online_learning_platform.auth.dto.CreateUpdateUserDto;
import com.rose.online_learning_platform.auth.entities.Role;
import com.rose.online_learning_platform.auth.entities.User;
import com.rose.online_learning_platform.auth.repository.RoleRepository;
import com.rose.online_learning_platform.auth.repository.UserRepository;
import com.rose.online_learning_platform.commons.enums.CourseStatusEnum;
import com.rose.online_learning_platform.commons.enums.RolesEnum;
import com.rose.online_learning_platform.course.entity.Course;
import com.rose.online_learning_platform.course.repository.CourseRepository;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.student.entity.Student;
import com.rose.online_learning_platform.student.repository.StudentRepository;
import com.rose.online_learning_platform.student.request.StudentRequest;
import com.rose.online_learning_platform.student.response.StudentResponse;
import lombok.AllArgsConstructor;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public Optional<Student> getStudentById(Long studentId) {

        return studentRepository.findById(studentId);
    }

    public String deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
        return "Student with " + studentId + "has been deleted";
    }




    public StudentResponse createStudent(CreateUpdateUserDto createUpdateUserDto) throws EMException {
        Optional<User> existingUserByEmail = userRepository.findByEmail(createUpdateUserDto.getEmail());

        Student student = new Student();
        student.setEmail(createUpdateUserDto.getEmail());
        student.setFullName(createUpdateUserDto.getFullName());

        Optional<Role> optionalRole = roleRepository.findByName(RolesEnum.STUDENT);

        if (optionalRole.isEmpty()) {
            throw EMException.builder().message("Role not found").build();
        }

        if (existingUserByEmail.isPresent()) {
            User existingUser = existingUserByEmail.get();
            student.setUser(existingUser);
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
            student.setUser(newUser);
        }

        Student savedStudent = studentRepository.save(student);

        return StudentResponse.builder()
                .fullName(savedStudent.getFullName())
                .email(savedStudent.getEmail())
                .build();
    }
    public StudentResponse createStudentWithCourse(StudentRequest studentRequest) {

        Student student = new Student();
        student.setEmail(studentRequest.getEmail());
        student.setFullName(studentRequest.getFullName());

        Student savedStudent = studentRepository.save(student);
        return StudentResponse.builder()
                 .fullName(savedStudent.getFullName())
                 .email(savedStudent.getEmail())
                 .build();
    }

    public List<Student> getALlStudents(){

        return studentRepository.findAll();
    }

}
