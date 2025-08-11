package dev.sv.cloud_file_storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequestDto(@NotBlank(message = "Username must contain at least 8 symbols")
                             @Size(min = 8, max = 255)
                             String username,
                             @NotBlank(message = "Password must contain at least 8 symbols")
                             @Size(min = 8, max = 255)
                             String password) {
}
