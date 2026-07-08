package com.qww.products.service;

import com.qww.products.common.UnauthorizedException;
import com.qww.products.domain.User;
import com.qww.products.dto.LoginRequest;
import com.qww.products.dto.LoginResponse;
import com.qww.products.dto.LoginUserResponse;
import com.qww.products.repository.UserRepository;
import com.qww.products.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String INVALID_CREDENTIALS = "invalid username or password";

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername().trim())
                .orElseThrow(() -> new UnauthorizedException(INVALID_CREDENTIALS));

        if (!user.isEnabled() || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException(INVALID_CREDENTIALS);
        }

        return LoginResponse.builder()
                .token(jwtTokenProvider.createToken(user))
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationSeconds())
                .user(LoginUserResponse.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .displayName(user.getDisplayName())
                        .role(user.getRole())
                        .build())
                .build();
    }
}
