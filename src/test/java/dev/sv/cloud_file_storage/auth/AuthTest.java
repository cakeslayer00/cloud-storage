package dev.sv.cloud_file_storage.auth;

import dev.sv.cloud_file_storage.config.AuthTestConfig;
import dev.sv.cloud_file_storage.config.PostgreSQLContainer;
import dev.sv.cloud_file_storage.dto.AuthRequestDto;
import dev.sv.cloud_file_storage.entity.User;
import dev.sv.cloud_file_storage.exception.UsernameAlreadyTakenException;
import dev.sv.cloud_file_storage.repository.UserRepository;
import dev.sv.cloud_file_storage.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DataJpaTest
@Testcontainers
@Import(AuthTestConfig.class)
public class AuthTest implements PostgreSQLContainer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

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
