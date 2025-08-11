package dev.sv.cloud_file_storage.controller;

import dev.sv.cloud_file_storage.dto.AuthRequestDto;
import dev.sv.cloud_file_storage.dto.AuthResponseDto;
import dev.sv.cloud_file_storage.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto signUp(@Valid @RequestBody AuthRequestDto requestDto) {
        return authService.register(requestDto);
    }

    @PostMapping("sign-in")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDto signIn(@Valid @RequestBody AuthRequestDto requestDto) {
        return authService.authenticate(requestDto);
    }

}
