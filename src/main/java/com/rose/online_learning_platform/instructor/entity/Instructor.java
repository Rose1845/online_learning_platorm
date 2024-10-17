package com.rose.online_learning_platform.instructor.entity;

import com.rose.online_learning_platform.auth.entities.User;
import com.rose.online_learning_platform.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    @OneToMany(mappedBy = "instructor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> courses;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
