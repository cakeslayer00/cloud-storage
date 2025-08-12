package dev.sv.cloud_file_storage.service;

import dev.sv.cloud_file_storage.config.MinioProperties;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioProperties properties;
    private final MinioClient minioClient;

    @Override
    public void removeObject(String path) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.bucketName())
                    .object(path)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getObject(String path) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(properties.bucketName())
                            .object(path)
                            .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StatObjectResponse statObject(String path) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(properties.bucketName())
                            .object(path)
                            .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyObject(String target, String source) {
        try {
            minioClient.copyObject(CopyObjectArgs.builder()
                    .bucket(properties.bucketName())
                    .object(target)
                    .source(
                            CopySource.builder()
                                    .bucket(properties.bucketName())
                                    .object(source)
                                    .build())
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectWriteResponse putObject(String path, MultipartFile file) {
        try {
            return minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.bucketName())
                    .object(path)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (IOException | ServerException | InsufficientDataException | ErrorResponseException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectWriteResponse putEmptyObject(String path) {
        try {
            return minioClient.putObject(PutObjectArgs.builder()
                    .bucket(properties.bucketName())
                    .object(path)
                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                    .build());
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterable<Result<Item>> listObjects(String path, boolean recursive) {
        return minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(properties.bucketName())
                .prefix(path)
                .recursive(recursive)
                .build());
    }

    @Override
    public boolean objectExists(String path) {
        try {
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(properties.bucketName())
                    .object(path)
                    .build());
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            throw new RuntimeException(e);
        }
    }
}
