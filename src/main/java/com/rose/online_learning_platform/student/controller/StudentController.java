package com.rose.online_learning_platform.student.controller;

import com.rose.online_learning_platform.auth.dto.CreateUpdateUserDto;
import com.rose.online_learning_platform.course.entity.Course;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.instructor.service.InstructorService;
import com.rose.online_learning_platform.student.entity.Student;
import com.rose.online_learning_platform.student.request.StudentRequest;
import com.rose.online_learning_platform.student.response.StudentResponse;
import com.rose.online_learning_platform.student.service.StudentService;
import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/students")
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class StudentController {

    private final StudentService studentService;
    private final InstructorService instructorService;
    @PostMapping()
    public StudentResponse createStudentWithCourse(@RequestBody CreateUpdateUserDto studentRequest) throws EMException {
        return studentService.createStudent(studentRequest);
    }


    @GetMapping
    public List<Student> printAllStudents(){
        return studentService.getALlStudents();
    }
    @GetMapping("student")
    public Optional<Student> getStudent(@RequestParam("id") Long studentId){
        return studentService.getStudentById(studentId);
    }
    @DeleteMapping("student")
    public String deleteStudent(@RequestParam("id") Long studentId){
        return studentService.deleteStudent(studentId);
    }

}
