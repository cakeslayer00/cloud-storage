package dev.sv.cloud_file_storage.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {

    @Bean
    public MinioClient minioClient(MinioProperties properties) throws Exception {
        MinioClient client = new MinioClient.Builder()
                .endpoint(properties.endpoint())
                .credentials(properties.username(), properties.password())
                .build();

        boolean bucketExists = client.bucketExists(BucketExistsArgs.builder().bucket(properties.bucketName()).build());
        if (!bucketExists) {
            client.makeBucket(MakeBucketArgs.builder().bucket(properties.bucketName()).build());
        }

        return client;
    }

}
