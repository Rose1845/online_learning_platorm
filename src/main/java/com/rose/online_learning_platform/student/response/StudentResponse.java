package com.rose.online_learning_platform.student.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class StudentResponse {
    private String fullName;
    private String email;
}
