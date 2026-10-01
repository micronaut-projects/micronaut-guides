package io.micronaut.guides;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuideProjectGeneratorTest {

    @TempDir
    Path directory;

    @Test
    void packageCleanupDoesNotTraverseVirtualEnvironmentSymlinks() throws Exception {
        Path project = Files.createDirectory(directory.resolve("project"));
        Path external = Files.createDirectory(directory.resolve("site-packages"));
        Files.createSymbolicLink(project.resolve(".venv"), external);
        Path externalMarker = Files.writeString(external.resolve("__init__.py"), "package initializer\n");
        Path sourceMarker = Files.writeString(project.resolve("__init__.py"), "");
        Path linkedPackage = Files.createDirectory(project.resolve("linked-package"));
        Path linkedMarker = Files.createSymbolicLink(linkedPackage.resolve("__init__.py"), externalMarker);

        Method cleanup = GuideProjectGenerator.class.getDeclaredMethod("removePythonPackageMarkerFiles", File.class);
        cleanup.setAccessible(true);
        cleanup.invoke(null, project.toFile());

        assertFalse(Files.exists(sourceMarker));
        assertTrue(Files.isSymbolicLink(linkedMarker));
        assertEquals("package initializer\n", Files.readString(externalMarker));
    }

    @Test
    void licensingDoesNotModifyFilesThroughSymlinks() throws Exception {
        Path project = Files.createDirectory(directory.resolve("project"));
        Path source = Files.createDirectories(project.resolve("src/example"));
        Path external = Files.createDirectory(directory.resolve("external"));
        Files.createSymbolicLink(project.resolve(".venv"), external);
        Path externalJava = Files.writeString(external.resolve("External.java"), "class External {}\n");
        Path sourceJava = Files.writeString(source.resolve("Application.java"), "class Application {}\n");
        Files.createSymbolicLink(project.resolve("Linked.java"), externalJava);

        try (GuideProjectGenerator generator = new GuideProjectGenerator()) {
            generator.addLicenses(project.toFile());
        }

        assertTrue(Files.readString(sourceJava).contains("Licensed under"));
        assertEquals("class External {}\n", Files.readString(externalJava));
    }
}
