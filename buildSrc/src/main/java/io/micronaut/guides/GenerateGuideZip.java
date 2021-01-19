package io.micronaut.guides;

import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GenerateGuideZip {

    public static void compressZipfile(String sourceDir, String outputFile) throws IOException, FileNotFoundException {
        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
        compressDirectoryToZipfile(sourceDir, sourceDir, zipFile);
        IOUtils.closeQuietly(zipFile);
    }

    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
        for (File file : new File(sourceDir).listFiles()) {
            if (file.isDirectory()) {
                compressDirectoryToZipfile(rootDir, sourceDir + File.separator + file.getName(), out);
            } else {
                ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + File.separator + file.getName());
                out.putNextEntry(entry);

                Path p = Paths.get(sourceDir, file.getName());
                FileInputStream in = new FileInputStream(p.toFile());
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
            }
        }
    }
}
