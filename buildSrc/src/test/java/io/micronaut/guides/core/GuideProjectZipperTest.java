package io.micronaut.guides.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class GuideProjectZipperTest {

    @Inject
    GuideProjectZipper guideProjectZipper;

    @Test
    void testZip() throws IOException {
        String projectFolder = "src/test/resources/guides/creating-your-first-micronaut-app";
        File projectFile = new File(projectFolder);
        File zipFile = new File(projectFolder + ".zip");

        guideProjectZipper.zipDirectory(projectFile, zipFile);

        List<String> expected = List.of("creating-your-first-micronaut-app/metadata.json", "creating-your-first-micronaut-app/creating-your-first-micronaut-app.adoc");
        List<String> result = new LinkedList<>();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                result.add(zipEntry.getName());
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }

        assertEquals(expected, result);
    }
}
