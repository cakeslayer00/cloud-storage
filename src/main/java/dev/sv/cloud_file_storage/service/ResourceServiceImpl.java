package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.dto.ResourceDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Override
    public ResourceDto getResource(String path) {
        return null;
    }

    @Override
    public void deleteResource(String path) {

    }

    @Override
    public void downloadResource(String path, HttpServletResponse response) {

    }

    @Override
    public ResourceDto moveResource(String from, String to) {
        return null;
    }

    @Override
    public ResourceDto searchResource(String query) {
        return null;
    }

    @Override
    public ResourceDto uploadResource(String path, MultipartFile[] files) {
        return null;
    }
}
