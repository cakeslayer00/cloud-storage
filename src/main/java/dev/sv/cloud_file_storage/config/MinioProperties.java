package dev.sv.cloud_file_storage.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.minio")
public record MinioProperties(String endpoint,
                              String username,
                              String password,
                              String bucketName) {
}
