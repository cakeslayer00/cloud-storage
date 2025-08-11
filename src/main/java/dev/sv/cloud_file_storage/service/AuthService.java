package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.dto.AuthRequestDto;
import dev.sv.cloud_file_storage.dto.AuthResponseDto;

public interface AuthService {

    AuthResponseDto authenticate(AuthRequestDto requestDto);

    AuthResponseDto register(AuthRequestDto requestDto);

}
