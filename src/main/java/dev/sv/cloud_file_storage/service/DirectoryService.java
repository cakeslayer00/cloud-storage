package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.entity.User;

public interface DirectoryService {

    ResourceDto createDirectory(String path);

    void createUserDirectory(User user);

    ResourceDto getDirectory(String path);

}
