package dev.sv.cloud_file_storage.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;

public interface PostgreSQLContainer {

    @Container
    @ServiceConnection
    org.testcontainers.containers.PostgreSQLContainer<?> postgres = new org.testcontainers.containers.PostgreSQLContainer<>("postgres:17.5-alpine3.22");

}
