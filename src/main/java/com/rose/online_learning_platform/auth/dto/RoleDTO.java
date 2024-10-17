package com.rose.online_learning_platform.auth.dto;

import com.rose.online_learning_platform.commons.enums.RolesEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class RoleDTO {
    private Long id;
    @NotNull(message = "Role name is required")
    private RolesEnum name;
    private Boolean isDeleted;
    private String description;
    private Date createdAt;
    private Date updatedAt;
}
