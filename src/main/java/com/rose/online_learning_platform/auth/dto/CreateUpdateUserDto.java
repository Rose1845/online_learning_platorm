package com.rose.online_learning_platform.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CreateUpdateUserDto {
    private String id;
    private String fullName;
    private String email;
    private String password;
    private Boolean  acceptTerms;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isActive;
    private String createdAt;
    private String updatedAt;
}
