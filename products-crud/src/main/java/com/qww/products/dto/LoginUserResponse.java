package com.qww.products.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginUserResponse {
    Long userId;
    String username;
    String displayName;
    String role;
}
