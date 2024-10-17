package com.rose.online_learning_platform.auth.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class JwtTokenOutput {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Date expiresIn;
    private Date issuedAt;
}
