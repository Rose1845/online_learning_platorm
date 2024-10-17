package com.rose.online_learning_platform.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthTokenDto {
    @NotNull(message = "Access token is required")
    @NotBlank(message = "Access token is required")
    private String accessToken;
}
