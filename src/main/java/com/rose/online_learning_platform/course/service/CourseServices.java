package com.rose.online_learning_platform.course.service;

import com.rose.online_learning_platform.commons.enums.CourseStatusEnum;
import com.rose.online_learning_platform.course.entity.Course;
import com.rose.online_learning_platform.course.repository.CourseRepository;
import com.rose.online_learning_platform.course.request.CourseRequest;
import com.rose.online_learning_platform.course.response.CourseResponse;
import com.rose.online_learning_platform.handlers.EMException;
import com.rose.online_learning_platform.instructor.entity.Instructor;
import com.rose.online_learning_platform.instructor.repository.InstructorRepository;
import com.rose.online_learning_platform.student.entity.Student;
import com.rose.online_learning_platform.student.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class CourseServices {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;

    public void enrollStudentInCourse(Long studentId, Long courseId) throws EMException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EMException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EMException("Course not found"));
        course.getStudents().add(student);
        studentRepository.save(student);
        courseRepository.save(course);
    }

    public Course createCourse(Long instructorId, CourseRequest courseDto) throws EMException {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() ->  EMException.builder().message("Instructor not found").build());
        Course course = new Course();
        course.setCourseName(courseDto.getCourseName());
        course.setStatusEnum(CourseStatusEnum.Pending);
        course.setDescription(courseDto.getDescription());
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    public Course updateCourse(Long courseId, Long instructorId, CourseRequest courseDto) throws EMException {
        // Fetch the existing course
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> EMException.builder().message("Course not found").build());

        // Verify that the instructor is the one who created the course or is authorized to update it
        if (!existingCourse.getInstructor().getId().equals(instructorId)) {
            throw EMException.builder().message("You are not authorized to update this course").build();
        }

        // Update course details
        existingCourse.setCourseName(courseDto.getCourseName());
        existingCourse.setDescription(courseDto.getDescription());
        existingCourse.setStatusEnum(CourseStatusEnum.valueOf(courseDto.getStatusEnum())); // If you want to allow status updates

        return courseRepository.save(existingCourse);
    }


    public Course approveCourseStatus(Long courseId) throws EMException {
        // Fetch the existing course
        Course existingCourse = courseRepository.findById(courseId)
                .orElseThrow(() -> EMException.builder().message("Course not found").build());


        // Check if the current status is Pending
        if (existingCourse.getStatusEnum() != CourseStatusEnum.Pending) {
            throw EMException.builder().message("Course can only be approved from Pending status").build();
        }

        // Update course status to Approved
        existingCourse.setStatusEnum(CourseStatusEnum.Approved);
        return courseRepository.save(existingCourse);
    }


    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }
    public String deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
        return "deleted successfully";
    }

    public Optional<Course> getAllStudentsByCourseName(String courseName) {
        return courseRepository.findAllStudentsByCourseName(courseName);
    }
}
