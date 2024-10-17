package com.rose.online_learning_platform.student.entity;

import com.rose.online_learning_platform.auth.entities.User;
import com.rose.online_learning_platform.course.entity.Course;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Course> course;
    @OneToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

}
