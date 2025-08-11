package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.dto.ResourceDto;

public interface DirectoryService {

    ResourceDto createDirectory(String path);

    ResourceDto getDirectory(String path);

}
