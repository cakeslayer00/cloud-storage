package dev.sv.cloud_file_storage.config;

import dev.sv.cloud_file_storage.repository.UserRepository;
import dev.sv.cloud_file_storage.service.AuthService;
import dev.sv.cloud_file_storage.service.ResourceService;
import dev.sv.cloud_file_storage.service.impl.AuthServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class AuthTestConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthService authService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return new AuthServiceImpl(
                mock(AuthenticationManager.class),
                mock(SecurityContextRepository.class),
                passwordEncoder,
                userRepository,
                mock(ResourceService.class));
    }

}
