package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResourceService {

    ResourceDto getResource(String path, Long userId);

    void deleteResource(String path, Long userId) throws RuntimeException;

    void downloadResource(String path, Long userId, HttpServletResponse response);

    ResourceDto moveResource(String from, String to, Long userId);

    List<ResourceDto> searchResource(String query, Long userId);

    List<ResourceDto> uploadResource(String path, MultipartFile[] files, Long userId);

    ResourceDto createDirectory(String path, Long userId);

    List<ResourceDto> getDirectory(String path, Long userId);

    void createUserDirectory(User user);

}
