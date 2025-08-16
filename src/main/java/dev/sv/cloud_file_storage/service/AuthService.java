package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.dto.AuthRequestDto;
import dev.sv.cloud_file_storage.dto.AuthResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    AuthResponseDto authenticate(AuthRequestDto requestDto,
                                 HttpServletRequest request,
                                 HttpServletResponse response);

    AuthResponseDto register(AuthRequestDto requestDto,
                             HttpServletRequest request,
                             HttpServletResponse response);

}
