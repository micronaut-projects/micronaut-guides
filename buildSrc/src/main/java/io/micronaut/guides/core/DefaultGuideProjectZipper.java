package io.micronaut.guides.core;

import jakarta.inject.Singleton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Singleton
public class DefaultGuideProjectZipper implements GuideProjectZipper {
    private static final List<String> EXCLUDED_FILES = List.of(".idea", ".DS_Store");

    private static void zipDirectoryContents(File folder, String baseName, ZipOutputStream zipOutputStream) throws IOException {
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (EXCLUDED_FILES.contains(file.getName())) {
                continue;
            }
            if (file.isDirectory()) {
                zipDirectoryContents(file, baseName + "/" + file.getName(), zipOutputStream);
            } else {
                try (FileInputStream fis = new FileInputStream(file)) {
                    String zipEntryName = baseName + "/" + file.getName();
                    zipOutputStream.putNextEntry(new ZipEntry(zipEntryName));

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, length);
                    }
                    zipOutputStream.closeEntry();
                }
            }
        }
    }

    @Override
    public void zipDirectory(File folderToZip, File zipFile) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {
            zipDirectoryContents(folderToZip, folderToZip.getName(), zipOutputStream);
        }
    }

}
