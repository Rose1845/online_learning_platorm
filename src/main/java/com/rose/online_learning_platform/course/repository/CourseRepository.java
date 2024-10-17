package com.rose.online_learning_platform.course.repository;


import com.rose.online_learning_platform.commons.enums.CourseStatusEnum;
import com.rose.online_learning_platform.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    Course findByCourseName(String courseName);
    Optional<Course> findAllStudentsByCourseName(String courseName);

    List<Course> findByStatusEnum(CourseStatusEnum statusEnum);


}
