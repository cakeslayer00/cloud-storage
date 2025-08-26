package dev.sv.cloud_file_storage.config;

import org.testcontainers.junit.jupiter.Container;

public interface MinIOContainer {

    @Container
    org.testcontainers.containers.MinIOContainer minio = new org.testcontainers.containers.MinIOContainer("minio/minio:latest")
            .withUserName("root")
            .withPassword("password");

}
