package com.qww.products.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponse {
    String token;
    String tokenType;
    Long expiresIn;
    LoginUserResponse user;
}
