package com.qww.products.repository;

import com.qww.products.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findsSeededAdminUser() {
        User user = userRepository.findByUsername("admin").orElse(null);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo(1L);
        assertThat(user.getUsername()).isEqualTo("admin");
        assertThat(user.getDisplayName()).isEqualTo("Administrator");
        assertThat(user.getRole()).isEqualTo("ADMIN");
        assertThat(user.isEnabled()).isTrue();
        assertThat(user.getPassword()).startsWith("$2");
        assertThat(new BCryptPasswordEncoder().matches("Admin@123456", user.getPassword())).isTrue();
    }
}
