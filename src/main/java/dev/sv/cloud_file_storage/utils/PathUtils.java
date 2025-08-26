package dev.sv.cloud_file_storage.utils;

import dev.sv.cloud_file_storage.exception.InvalidPathException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtils {

    /**
     * @param path Assuming path is normalized
     * @param id User identification number for user prefix
     * @return Fully assembled path with user prefix
     */
    public String appendRootPrefix(String path, Long id) {
        return "user-%s-files/%s".formatted(id, path);
    }

    /**
     * @param path User input
     * @return Normal path with no consecutive slashes
     * @throws InvalidPathException in case path contains forbidden characters
     */
    public String normalize(String path) {
        if (path.matches("[/\\\\\\x00-\\x1F\uFFFD]")) {
            throw new InvalidPathException("Invalid path or file name: %s".formatted(path));
        }
        return path
                .replaceAll("/{2,}", "/")
                .replaceFirst("^/", "");
    }

    public boolean isDirectory(String path) {
        return path.endsWith("/");
    }

}
