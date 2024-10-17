package com.rose.online_learning_platform.instructor.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class InstructorResponse {
    private String fullName;
    private String email;
}
