package com.qww.products.service;

import com.qww.products.common.UnauthorizedException;
import com.qww.products.dto.LoginRequest;
import com.qww.products.dto.LoginResponse;
import com.qww.products.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void logsInWithSeededAdminCredentials() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("Admin@123456");

        LoginResponse response = authService.login(request);

        assertThat(response.getToken()).isNotBlank();
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(86400L);
        assertThat(response.getUser().getUserId()).isEqualTo(1L);
        assertThat(response.getUser().getUsername()).isEqualTo("admin");
        assertThat(response.getUser().getDisplayName()).isEqualTo("Administrator");
        assertThat(response.getUser().getRole()).isEqualTo("ADMIN");
        assertThat(jwtTokenProvider.validateToken(response.getToken())).isTrue();
        assertThat(jwtTokenProvider.getUsername(response.getToken())).isEqualTo("admin");
    }

    @Test
    void rejectsInvalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong-password");

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("invalid username or password");
    }
}
