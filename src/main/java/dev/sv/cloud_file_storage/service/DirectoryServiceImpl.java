package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.config.MinioProperties;
import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.entity.User;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class DirectoryServiceImpl implements DirectoryService {

    private static final String USER_FOLDER = "user-%s-files/";

    private final MinioProperties properties;
    private final MinioClient minioClient;

    @Override
    public ResourceDto createDirectory(String path) {
        return null;
    }

    @Override
    public ResourceDto getDirectory(String path) {
        return null;
    }

    @Override
    public void createUserDirectory(User user) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.bucketName())
                    .object(USER_FOLDER.formatted(user.getId()))
                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }
}
