package com.rose.online_learning_platform.instructor.repository;


import com.rose.online_learning_platform.instructor.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor,Long> {

}
