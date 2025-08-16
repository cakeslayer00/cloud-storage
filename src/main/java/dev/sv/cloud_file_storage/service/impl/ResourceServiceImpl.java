package dev.sv.cloud_file_storage.service.impl;

import dev.sv.cloud_file_storage.dto.ResourceDto;
import dev.sv.cloud_file_storage.entity.User;
import dev.sv.cloud_file_storage.exception.InvalidFileNameException;
import dev.sv.cloud_file_storage.exception.InvalidOperationException;
import dev.sv.cloud_file_storage.exception.ResourceAlreadyExistsException;
import dev.sv.cloud_file_storage.exception.ResourceNotFoundException;
import dev.sv.cloud_file_storage.mapper.ResourceMapper;
import dev.sv.cloud_file_storage.service.MinioService;
import dev.sv.cloud_file_storage.service.ResourceService;
import dev.sv.cloud_file_storage.utils.Path;
import dev.sv.cloud_file_storage.utils.PathUtils;
import io.minio.ObjectWriteResponse;
import io.minio.Result;
import io.minio.StatObjectResponse;
import io.minio.errors.*;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static dev.sv.cloud_file_storage.utils.PathUtils.isDirectory;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private static final String DIRECTORY_ALREADY_EXISTS = "Directory '%s' already exists";
    private static final String RESOURCE_ALREADY_EXISTS = "Resource '%s' already exists";
    private static final String DIRECTORY_NOT_FOUND = "Directory under path '%s' doesn't exist";
    private static final String RESOURCE_NOT_FOUND = "Resource under path '%s' doesn't exist";
    private static final String INVALID_FILE_NAME = "Invalid file name";
    private static final String USER_PREFIX = "user-%s-files/";

    private final MinioService minioService;

    private final ResourceMapper resourceMapper;

    @Override
    public ResourceDto getResource(String path, Long userId) {
        Path total = new Path(path, userId);

        if (!minioService.objectExists(total.getAbsolutePath())) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND.formatted(total.getNormalPath()));
        }

        StatObjectResponse sor = minioService.statObject(total.getAbsolutePath());
        return resourceMapper.map(sor.size(), new Path(sor.object(), userId));
    }

    @Override
    public void deleteResource(String path, Long userId) {
        Path total = new Path(path, userId);

        if (!minioService.objectExists(total.getAbsolutePath())) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND.formatted(total.getNormalPath()));
        }

        if (total.isDirectory()) {
            deleteDirectory(total);
            return;
        }

        minioService.removeObject(total.getAbsolutePath());
    }

    @Override
    public void downloadResource(String path, Long userId, HttpServletResponse response) {
        Path total = new Path(path, userId);

        if (!minioService.objectExists(total.getAbsolutePath())) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND.formatted(total.getNormalPath()));
        }

        try {
            if (!total.isDirectory()) {
                downloadFile(path, userId, response);
            } else {
                zipDirectory(path, userId, response);
            }
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"%s\"".formatted(total.getFileName()));
        } catch (ServerException | InsufficientDataException | ErrorResponseException | IOException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidResponseException | XmlParserException |
                 InternalException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResourceDto moveResource(String from, String to, Long userId) {
        Path origin = new Path(from, userId);
        Path target = new Path(to, userId);

        if (!minioService.objectExists(origin.getAbsolutePath())) {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND.formatted(origin.getNormalPath()));
        }
        if (minioService.objectExists(target.getAbsolutePath())) {
            throw new ResourceAlreadyExistsException(RESOURCE_ALREADY_EXISTS);
        }

        if (origin.getPathWithoutPrefixAndFile().equals(target.getPathWithoutPrefixAndFile())) {
            rename(origin, target);
        } else {
            move(origin, target);
        }

        StatObjectResponse sor = minioService.statObject(target.getAbsolutePath());
        return resourceMapper.map(sor.size(), new Path(sor.object(), userId));
    }

    @Override
    public List<ResourceDto> searchResource(String query, Long userId) {
        Iterable<Result<Item>> results = minioService.listObjects(PathUtils.appendRootPrefix("", userId), true);
        List<ResourceDto> resources = new ArrayList<>();
        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                String object = item.objectName();
                if (object.contains(query)) {
                    resources.add(resourceMapper.map(item.size(), new Path(object, userId)));
                }
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                     XmlParserException e) {
                throw new RuntimeException(e);
            }
        }
        return resources;
    }

    @Override
    public List<ResourceDto> uploadResource(String path, MultipartFile[] files, Long userId) {
        Path total = new Path(path, userId);
        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (fileName == null) {
                throw new InvalidFileNameException(INVALID_FILE_NAME);
            }
            if (fileName.contains("/")) {
                createDirectoriesRecursively(userId, fileName);
            }
            if (minioService.objectExists(total.getAbsolutePath() + fileName)) {
                throw new ResourceAlreadyExistsException(RESOURCE_ALREADY_EXISTS.formatted(total.getNormalPath()) + fileName);
            }

            paths.add(total.getAbsolutePath() + fileName);
            minioService.putObject(total.getAbsolutePath() + fileName, file);
        }

        List<ResourceDto> resources = new ArrayList<>();
        for (String p : paths) {
            StatObjectResponse sor = minioService.statObject(p);
            resources.add(resourceMapper.map(sor.size(), new Path(sor.object(), userId)));
        }
        return resources;
    }

    @Override
    public ResourceDto createDirectory(String path, Long userId) {
        path = path.endsWith("/") ? path : path + "/";
        Path total = new Path(path, userId);

        if (minioService.objectExists(total.getAbsolutePath())) {
            throw new ResourceAlreadyExistsException(DIRECTORY_ALREADY_EXISTS.formatted(total.getNormalPath()));
        }

        ObjectWriteResponse owr = minioService.putEmptyObject(total.getAbsolutePath());
        return resourceMapper.map(null, new Path(owr.object(), userId));
    }

    @Override
    public List<ResourceDto> getDirectory(String path, Long userId) {
        Path total = new Path(path, userId);

        if (!minioService.objectExists(total.getAbsolutePath())) {
            throw new ResourceNotFoundException(DIRECTORY_NOT_FOUND.formatted(total.getNormalPath()));
        }

        Iterable<Result<Item>> results = minioService.listObjects(total.getAbsolutePath(), false);
        List<ResourceDto> resources = new ArrayList<>();
        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                if (item.objectName().equals(total.getAbsolutePath())) {
                    continue;
                }
                resources.add(resourceMapper.map(item.size(), new Path(item.objectName(), userId)));
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                     XmlParserException e) {
                throw new RuntimeException(e);
            }
        }
        return resources;
    }

    @Override
    public void createUserDirectory(User user) {
        minioService.putEmptyObject(USER_PREFIX.formatted(user.getId()));
    }

    private void deleteDirectory(Path total) {
        Iterable<Result<Item>> results = minioService.listObjects(total.getAbsolutePath(), true);
        for (Result<Item> result : results) {
            try {
                Item item = result.get();
                minioService.removeObject(item.objectName());
            } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                     InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                     XmlParserException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void rename(Path source, Path target) {
        if (source.getFileName().equals(target.getFileName())) {
            throw new InvalidOperationException("Target resource should have different name");
        }

        minioService.copyObject(target.getAbsolutePath(), source.getAbsolutePath());
        minioService.removeObject(source.getAbsolutePath());
    }

    private void move(Path source, Path target) {
        if (!source.getFileName().equals(target.getFileName())) {
            throw new InvalidOperationException("Target resource should have same name");
        }

        minioService.copyObject(target.getAbsolutePath(), source.getAbsolutePath());
        minioService.removeObject(source.getAbsolutePath());
    }

    private void downloadFile(String path, Long userId, HttpServletResponse response) throws IOException {
        Path total = new Path(path, userId);
        try (InputStream stream = minioService.getObject(total.getAbsolutePath())) {
            StreamUtils.copy(stream, response.getOutputStream());
        }
    }

    private void zipDirectory(String path, Long userId, HttpServletResponse response) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            Path dir = new Path(path, userId);
            Iterable<Result<Item>> results = minioService.listObjects(dir.getAbsolutePath(), true);

            for (Result<Item> result : results) {
                Item item = result.get();
                String object = item.objectName();

                if (dir.getAbsolutePath().equals(object) && isDirectory(object)) {
                    continue;
                }

                addZipEntry(userId, object, zos);
            }
        }
    }

    private void addZipEntry(Long userId, String object, ZipOutputStream zos) throws IOException {
        try (InputStream stream = minioService.getObject(object)) {
            String relativePath = object.substring(USER_PREFIX.formatted(userId).length());
            ZipEntry zipEntry = new ZipEntry(relativePath);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = stream.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
        }
    }

    private void createDirectoriesRecursively(Long userId, String fileName) {
        Path total = new Path(fileName, userId);

        StringBuilder sb = new StringBuilder();
        String[] split = total.getPathWithoutPrefixAndFile().split("/");
        for (String s : split) {
            sb.append(s).append("/");
            if (minioService.objectExists(total.getPrefix() + sb)) {
                continue;
            }
            createDirectory(sb.toString(), userId);
        }
    }

}
