package com.rose.online_learning_platform.course.request;

import com.rose.online_learning_platform.commons.enums.CourseStatusEnum;
import lombok.Data;

@Data
public class CourseRequest {
    private String courseName;
    private String description;
    private String statusEnum;

}
