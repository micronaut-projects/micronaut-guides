package io.micronaut.guides;

import org.apache.commons.compress.archivers.zip.UnixStat;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GenerateGuideZip {

    private static final List<String> EXECUTABLES = Arrays.asList("gradlew", "gradlew.bat", "mvnw", "mvnw.bat");

    public static void compressZipfile(String sourceDir, String outputFile) throws IOException {
        ZipArchiveOutputStream zipFile = new ZipArchiveOutputStream(new FileOutputStream(outputFile));
        compressDirectoryToZipfile(sourceDir, sourceDir, zipFile);
        IOUtils.closeQuietly(zipFile);
    }

    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipArchiveOutputStream out) throws IOException {
        for (File file : new File(sourceDir).listFiles()) {
            if (file.isDirectory()) {
                compressDirectoryToZipfile(rootDir, sourceDir + File.separatorChar + file.getName(), out);
            } else {
                ZipArchiveEntry entry = new ZipArchiveEntry(sourceDir.replace(rootDir, "") + File.separatorChar + file.getName());
                if (EXECUTABLES.contains(file.getName())) {
                    entry.setUnixMode(UnixStat.FILE_FLAG | 0755);
                }
                out.putArchiveEntry(entry);
                Path p = Paths.get(sourceDir, file.getName());
                FileInputStream in = new FileInputStream(p.toFile());
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
                out.closeArchiveEntry();
            }
        }
    }
}
