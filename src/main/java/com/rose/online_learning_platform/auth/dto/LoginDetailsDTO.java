package com.rose.online_learning_platform.auth.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDetailsDTO {
    @NotNull(message = "Password is required")
    @NotBlank(message = "Password is required")
    private String password;

    @Nullable
    @Email(message = "Invalid email")
    private String email;
}
