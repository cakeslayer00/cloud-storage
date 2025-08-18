package dev.sv.cloud_file_storage.controller;

import dev.sv.cloud_file_storage.dto.AuthResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponseDto getUser(Authentication authentication) {
        return new AuthResponseDto(authentication.getName());
    }

}
