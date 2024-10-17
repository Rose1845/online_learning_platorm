package com.rose.online_learning_platform.auth.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class UserDTO {
    private String id;
    private String fullName;
    private String email;
    private Boolean  acceptTerms;
    private Boolean isAccountNonExpired;
    private Boolean isAccountNonLocked;
    private Boolean isCredentialsNonExpired;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
}
