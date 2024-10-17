package com.rose.online_learning_platform.instructor.request;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class InstructorRequest {
    private String fullName;
    private String email;

}
