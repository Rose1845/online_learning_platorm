package com.rose.online_learning_platform.handlers;

import lombok.*;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EMException extends Throwable {
    public EMException(String message) {
        super(message);
    }
    private Integer statusCode = 500;
    private String metadata = "No metadata";
    private String message = "Internal server error";
}
