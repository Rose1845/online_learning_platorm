package com.rose.online_learning_platform.course.request;

import lombok.Data;

@Data
public class CourseRequest {
    private String courseName;
    private String description;
    private String statusEnum;

}
