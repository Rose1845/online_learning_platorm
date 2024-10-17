package com.rose.online_learning_platform.course.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private String courseName;
    private String description;
    private String statusEnum;

}
