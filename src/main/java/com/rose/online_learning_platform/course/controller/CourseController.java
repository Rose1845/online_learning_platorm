package com.rose.online_learning_platform.course.controller;

import com.rose.online_learning_platform.course.entity.Course;
import com.rose.online_learning_platform.course.request.CourseRequest;
import com.rose.online_learning_platform.course.response.CourseResponse;
import com.rose.online_learning_platform.course.service.CourseServices;
import com.rose.online_learning_platform.handlers.EMException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseServices courseServices;

    @PostMapping("/create/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Course> createCourse(@PathVariable Long instructorId, @RequestBody CourseRequest courseDto) {
        try {
            Course createdCourse = courseServices.createCourse(instructorId, courseDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
        } catch (EMException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{courseId}/update/{instructorId}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long courseId,
            @PathVariable Long instructorId,
            @RequestBody CourseRequest courseDto) {
        try {
            Course updatedCourse = courseServices.updateCourse(courseId, instructorId, courseDto);
            return ResponseEntity.ok(updatedCourse);
        } catch (EMException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{courseId}/approve")
    @PreAuthorize("hasRole('APPROVER')")
    public ResponseEntity<Course> approveCourse(
            @PathVariable Long courseId) {
        try {
            Course approvedCourse = courseServices.approveCourseStatus(courseId);
            return ResponseEntity.ok(approvedCourse);
        } catch (EMException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("")
    public List<Course> getAllCourses(){
        return courseServices.getAllCourses();
    }
    @GetMapping("course")
    public Optional<Course> getCourseById(@RequestParam("id") Long courseId){
        return courseServices.getCourseById(courseId);
    }
    @GetMapping("students")
    public Optional<Course> getAllStudentsByCourseName(@RequestParam("courseName") String courseName){
        return courseServices.getAllStudentsByCourseName(courseName);
    }
    @DeleteMapping("course")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public String deleteCourse(@RequestParam("id") Long courseId){
        return courseServices.deleteCourse(courseId);
    }

    @PostMapping("/{courseId}/enroll/{studentId}")
    public ResponseEntity<Void> enrollStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        try {
            courseServices.enrollStudentInCourse(studentId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EMException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



}
