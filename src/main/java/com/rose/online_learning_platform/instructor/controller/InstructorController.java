package com.rose.online_learning_platform.instructor.controller;

import com.rose.online_learning_platform.auth.dto.CreateUpdateUserDto;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.instructor.entity.Instructor;
import com.rose.online_learning_platform.instructor.response.InstructorResponse;
import com.rose.online_learning_platform.instructor.service.InstructorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/instructors")
public class InstructorController {

    private final InstructorService instructorService;

    public InstructorController(InstructorService instructorService) {
        this.instructorService = instructorService;
    }

    @PostMapping
    public ResponseEntity<InstructorResponse> createInstructor(@RequestBody CreateUpdateUserDto createUpdateUserDto) {
        try {
            InstructorResponse createdInstructor = instructorService.createInstructor(createUpdateUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInstructor);
        } catch (EMException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instructor> getInstructorById(@PathVariable Long id) {
        Optional<Instructor> instructor = instructorService.getTeacherById(id);
        return instructor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @GetMapping()
    public List<Instructor> getAllTeachers(){
        return instructorService.getAllTeachers();
    }
    @DeleteMapping()
    public String deleteTeacher(@RequestParam("id") Long teacherId){
        return instructorService.deleteTeacher(teacherId);
    }

}
