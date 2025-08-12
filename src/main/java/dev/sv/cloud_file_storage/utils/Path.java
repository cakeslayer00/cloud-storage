package dev.sv.cloud_file_storage.utils;

import lombok.Getter;

import static dev.sv.cloud_file_storage.utils.PathUtils.appendRootPrefix;
import static dev.sv.cloud_file_storage.utils.PathUtils.normalize;

public class Path {

    @Getter
    private final String prefix;
    private final String normal;
    private final String absolute;

    public Path(String path, Long id) {
        this.prefix = "user-%s-files/".formatted(id);

        if (path.contains(prefix)) {
            path = path.replace(prefix, "");
        }
        this.normal = normalize(path);
        this.absolute = appendRootPrefix(normal, id);
    }

    public boolean isDirectory() {
        return normal.endsWith("/");
    }

    public String getFileName() {
        if (isDirectory()) {
            int i = normal.length() - 2;
            while (i >= 0 && normal.charAt(i) != '/') i--;
            return normal.substring(i + 1);
        }
        return normal.substring(normal.lastIndexOf("/") + 1);
    }

    public String getPathWithoutPrefixAndFile() {
        return normal.substring(0, normal.length() - getFileName().length());
    }

    public String getAbsolutePath() {
        return absolute;
    }

    public String getNormalPath() {
        return normal;
    }

}
