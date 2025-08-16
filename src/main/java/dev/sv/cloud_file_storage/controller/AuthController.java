package dev.sv.cloud_file_storage.controller;

import dev.sv.cloud_file_storage.dto.AuthRequestDto;
import dev.sv.cloud_file_storage.dto.AuthResponseDto;
import dev.sv.cloud_file_storage.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public AuthResponseDto signUp(@Valid @RequestBody AuthRequestDto requestDto,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        return authService.register(requestDto, request, response);
    }

    @PostMapping("sign-in")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDto signIn(@Valid @RequestBody AuthRequestDto requestDto,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        return authService.authenticate(requestDto, request, response);
    }

}
