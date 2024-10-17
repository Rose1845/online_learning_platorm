package com.rose.online_learning_platform.course.entity;
import com.rose.online_learning_platform.commons.enums.CourseStatusEnum;
import com.rose.online_learning_platform.commons.enums.RolesEnum;
import com.rose.online_learning_platform.instructor.entity.Instructor;
import com.rose.online_learning_platform.student.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Entity
@Table(name="courses")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String courseName;
    private String description;
    @NotNull(message = "course status  is required")
    private CourseStatusEnum statusEnum;
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;
    @ManyToMany(fetch = FetchType.EAGER,cascade ={CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name = "student_courses", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> students;
}