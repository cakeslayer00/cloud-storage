package dev.sv.cloud_file_storage.mapper;

import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.utils.Path;
import org.springframework.stereotype.Component;

@Component
public class ResourceMapper {

    public ResourceDto map(Long size, Path path) {
        return new ResourceDto(
                path.getPathWithoutPrefixAndFile(),
                path.getFileName(),
                size == null || size == 0 ? null : size,
                path.isDirectory() ? "DIRECTORY" : "FILE");
    }

}
