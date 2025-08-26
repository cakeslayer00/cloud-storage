package dev.sv.cloud_file_storage.utils;

import org.apache.commons.lang3.StringUtils;

import static dev.sv.cloud_file_storage.utils.PathUtils.appendRootPrefix;
import static dev.sv.cloud_file_storage.utils.PathUtils.normalize;

public class Path {

    private static final String USER_PREFIX_FORMAT = "user-%s-files/";
    private static final String DIRECTORY_SEPARATOR = "/";

    private final String normal;
    private final String absolute;

    public Path(String path, Long id) {
        String prefix = USER_PREFIX_FORMAT.formatted(id);

        if (path.contains(prefix)) {
            path = path.replace(prefix, StringUtils.EMPTY);
        }
        this.normal = normalize(path);
        this.absolute = appendRootPrefix(normal, id);
    }

    public boolean isDirectory() {
        return normal.endsWith(DIRECTORY_SEPARATOR);
    }

    public String getFileName() {
        if (isDirectory()) {
            int i = normal.length() - 2;
            while (i >= 0 && normal.charAt(i) != '/') i--;
            return normal.substring(i + 1);
        }
        return normal.substring(normal.lastIndexOf(DIRECTORY_SEPARATOR) + 1);
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
