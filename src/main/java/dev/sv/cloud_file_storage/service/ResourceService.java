package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.dto.ResourceDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceService {

    ResourceDto getResource(String path);

    void deleteResource(String path);

    void downloadResource(String path, HttpServletResponse response);

    ResourceDto moveResource(String from, String to);

    ResourceDto searchResource(String query);

    ResourceDto uploadResource(String path, MultipartFile[] files);

}
