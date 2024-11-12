package io.micronaut.guides.core;

import jakarta.inject.Singleton;
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
import java.util.List;

@Singleton
public class DefaultGuideProjectZipper implements GuideProjectZipper {
    private static final List<String> EXCLUDED_FILES = List.of(".idea", ".DS_Store");
    private static final List<String> EXECUTABLES = List.of("gradlew", "gradlew.bat", "mvnw", "mvnw.bat");

    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipArchiveOutputStream out) throws IOException {
        for (File file : new File(sourceDir).listFiles()) {
            if (EXCLUDED_FILES.contains(file.getName())) {
                continue;
            }
            if (file.isDirectory()) {
                compressDirectoryToZipfile(rootDir, sourceDir + File.separatorChar + file.getName(), out);
            } else {
                String zipPath = sourceDir.replace(rootDir, "") + '/' + file.getName();
                if (zipPath.charAt(0) == '/') {
                    zipPath = zipPath.substring(1);
                }
                ZipArchiveEntry entry = new ZipArchiveEntry(zipPath);
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

    @Override
    public void zipDirectory(String sourceDir, String outputFile) throws IOException {
        ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(new FileOutputStream(outputFile));
        compressDirectoryToZipfile(sourceDir, sourceDir, zipOutputStream);
        IOUtils.closeQuietly(zipOutputStream);
    }

}
