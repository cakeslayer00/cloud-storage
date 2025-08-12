package dev.sv.cloud_file_storage.service;

import io.minio.*;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface MinioService {

    void removeObject(String path);

    InputStream getObject(String path);

    StatObjectResponse statObject(String path);

    void copyObject(String target, String source);

    ObjectWriteResponse putObject(String path, MultipartFile file);

    ObjectWriteResponse putEmptyObject(String path);

    Iterable<Result<Item>> listObjects(String path, boolean recursive);

    boolean objectExists(String path);
}
