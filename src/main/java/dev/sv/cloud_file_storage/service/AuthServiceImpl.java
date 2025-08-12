package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.dto.AuthRequestDto;
import dev.sv.cloud_file_storage.dto.AuthResponseDto;
import dev.sv.cloud_file_storage.entity.User;
import dev.sv.cloud_file_storage.exception.InvalidFormInputException;
import dev.sv.cloud_file_storage.exception.UsernameAlreadyTakenException;
import dev.sv.cloud_file_storage.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String USERNAME_IS_ALREADY_TAKEN = "Username '%s' is already taken";
    private static final String INVALID_USERNAME_OR_PASSWORD = "Invalid username or password";

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final ResourceService service;

    @Override
    @Transactional
    public AuthResponseDto authenticate(AuthRequestDto requestDto) {
        try {
            UsernamePasswordAuthenticationToken unauthenticated =
                    UsernamePasswordAuthenticationToken.unauthenticated(requestDto.username(), requestDto.password());
            Authentication authenticate = authenticationManager.authenticate(unauthenticated);
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticate);

            SecurityContextHolder.setContext(context);
        } catch (BadCredentialsException e) {
            throw new InvalidFormInputException(INVALID_USERNAME_OR_PASSWORD);
        }
        return new AuthResponseDto(requestDto.username());
    }

    @Override
    @Transactional
    public AuthResponseDto register(AuthRequestDto requestDto) {
        try {
            User user = new User();
            user.setUsername(requestDto.username());
            user.setPassword(passwordEncoder.encode(requestDto.password()));
            userRepository.save(user);

            service.createUserDirectory(user);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyTakenException(USERNAME_IS_ALREADY_TAKEN.formatted(requestDto.username()));
        }

        return authenticate(requestDto);
    }

}
