package dev.sv.cloud_file_storage.user;

import dev.sv.cloud_file_storage.dto.AuthRequestDto;
import dev.sv.cloud_file_storage.entity.User;
import dev.sv.cloud_file_storage.exception.UsernameAlreadyTakenException;
import dev.sv.cloud_file_storage.repository.UserRepository;
import dev.sv.cloud_file_storage.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Testcontainers
@Import(AuthTestConfig.class)
public class AuthTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.5-alpine3.22");

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void givenValidRequest_whenRegister_thenUserSavedToDatabase() {
        userRepository.deleteAll();

        AuthRequestDto request = new AuthRequestDto("username", "password");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        authService.register(request, mockRequest, mockResponse);

        assertTrue(userRepository.existsByUsername(request.username()));

        assertTrue(userRepository.existsByUsername(request.username()));
        User user = userRepository.findByUsername(request.username()).orElseThrow();
        assertNotNull(user.getPassword());
        assertNotEquals("password", user.getPassword());
    }

    @Test
    void givenExistingUsername_whenRegister_thenThrowsException() {
        userRepository.deleteAll();

        AuthRequestDto request = new AuthRequestDto("username", "password");
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);

        authService.register(request, mockRequest, mockResponse);

        assertThrows(UsernameAlreadyTakenException.class,
                () -> authService.register(request, mock(HttpServletRequest.class), mock(HttpServletResponse.class)));
    }

}
