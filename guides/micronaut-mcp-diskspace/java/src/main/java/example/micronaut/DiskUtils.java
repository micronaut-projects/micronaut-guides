package example.micronaut;

import java.io.File;

public final class DiskUtils {
    private DiskUtils() {}

    public static String freeDiskSpace() {
        File root = new File("/");
        long freeBytes = root.getFreeSpace();
        double freeGB = freeBytes / (1024.0 * 1024 * 1024);
        return String.format("Free disk space: %.2f GB", freeGB);
    }
}

