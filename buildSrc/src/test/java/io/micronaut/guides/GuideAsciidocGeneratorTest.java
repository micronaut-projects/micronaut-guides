package io.micronaut.guides;

import io.micronaut.guides.core.App;
import io.micronaut.guides.core.Guide;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuideAsciidocGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void onlyForLanguagesEndsAtClosingMarker() throws Exception {
        Path input = Files.createDirectory(tempDir.resolve("guide"));
        Path output = Files.createDirectory(tempDir.resolve("output"));
        Path project = Files.createDirectory(tempDir.resolve("project"));
        Files.createDirectories(project.resolve("buildSrc/src/main/resources"));
        Files.writeString(project.resolve("buildSrc/src/main/resources/version.txt"), "5.2.0-SNAPSHOT");
        Files.createDirectories(project.resolve("src/docs/common/snippets"));
        Files.writeString(project.resolve("src/docs/common/snippets/common-license.adoc"), "license");
        Files.writeString(input.resolve("guide.adoc"), """
                before
                :only-for-languages:python
                python-only
                :only-for-languages:
                after
                """);

        Guide guide = new Guide(
                "Guide", "Guide.", List.of("Micronaut"), List.of("Core Basics"), LocalDate.of(2026, 9, 22),
                null, null, null, false, false, "guide.adoc", List.of(Language.JAVA, Language.PYTHON), List.of(),
                List.of(BuildTool.GRADLE, BuildTool.PYRONAUT), TestFramework.JUNIT, List.of(), "guide", true, null,
                Map.of(), List.of(new App("default", null, null, null, List.of(), null, null, null, null, null, null, null, true)), true, false);

        GuideAsciidocGenerator.generate(guide, input.toFile(), output.toFile(), project.toFile());

        String java = Files.readString(output.resolve("guide-gradle-java.adoc"));
        String python = Files.readString(output.resolve("guide-pyronaut-python.adoc"));
        assertTrue(java.contains("before"));
        assertFalse(java.contains("python-only"));
        assertTrue(java.contains("after"));
        assertTrue(python.contains("before"));
        assertTrue(python.contains("python-only"));
        assertTrue(python.contains("after"));
    }
}
