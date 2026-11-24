package io.micronaut.guides;

import io.micronaut.guides.core.App;
import io.micronaut.guides.core.Guide;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuideAsciidocGeneratorTest {

    @TempDir
    Path tempDir;

    @Test
    void onlyForLanguagesFiltersContentAndUsesPythonProjectPaths() throws Exception {
        Path inputDir = Files.createDirectory(tempDir.resolve("input"));
        Path outputDir = Files.createDirectory(tempDir.resolve("output"));
        Files.writeString(inputDir.resolve("macro-test.adoc"), """
                Before

                @guideTitle@
                @guideIntro@
                Authors: @authors@
                Micronaut Version: @micronaut@
                mn @cli-command@ example.micronaut.micronautguide
                
                :only-for-languages:python
                Python only
                source:HelloController[]
                test:HelloControllerTest[]
                pyronaut @cli-command@ example.micronaut.micronautguide --features=@features@
                :only-for-languages:
                
                After
                """);

        Guide guide = javaAndPythonGuide("macro-test.adoc", "macro-test");

        GuideAsciidocGenerator.generate(guide, inputDir.toFile(), outputDir.toFile(), Path.of("..").toAbsolutePath().toFile());

        String javaOutput = Files.readString(outputDir.resolve("macro-test-gradle-java.adoc"));
        String pythonOutput = Files.readString(outputDir.resolve("macro-test-pyronaut-python.adoc"));

        assertFalse(javaOutput.contains("Python only"));
        assertFalse(javaOutput.contains(":only-for-languages:"));
        assertTrue(javaOutput.contains("Micronaut application"));
        assertTrue(javaOutput.contains("Authors: Author"));
        assertTrue(javaOutput.contains("Micronaut Version:"));
        assertTrue(javaOutput.contains("mn create-app example.micronaut.micronautguide"));
        assertTrue(pythonOutput.contains("Python only"));
        assertFalse(pythonOutput.contains(":only-for-languages:"));
        assertFalse(pythonOutput.contains("Authors:"));
        assertFalse(pythonOutput.contains("Micronaut Version:"));
        assertTrue(pythonOutput.contains("mn create example.micronaut.micronautguide"));
        assertFalse(pythonOutput.contains("mn create-app example.micronaut.micronautguide"));
        assertTrue(pythonOutput.contains("pyronaut create example.micronaut.micronautguide"));
        assertFalse(pythonOutput.contains("--features="));
        assertTrue(pythonOutput.contains("Pyronaut application guide"));
        assertTrue(pythonOutput.contains("Description for a Pyronaut application."));
        assertTrue(pythonOutput.contains("Pyronaut application"));
        assertFalse(pythonOutput.contains("Micronaut application"));
        assertTrue(pythonOutput.contains("src/example/micronaut/hello_controller.py"));
        assertTrue(pythonOutput.contains("tests/example/micronaut/test_hello_controller.py"));
    }

    @Test
    void pyronautOptionPrefersPyronautCalloutAndCommonVariants() throws Exception {
        Path projectDir = Files.createDirectory(tempDir.resolve("project"));
        Path callouts = Files.createDirectories(projectDir.resolve("src/docs/common/callouts"));
        Path snippets = Files.createDirectories(projectDir.resolve("src/docs/common/snippets"));
        Files.createDirectories(projectDir.resolve("buildSrc/src/main/resources"));
        Files.writeString(projectDir.resolve("buildSrc/src/main/resources/version.txt"), "1.0.0");
        Files.writeString(callouts.resolve("callout-both.adoc"), "<.> JVM both {0}");
        Files.writeString(callouts.resolve("callout-both-pyronaut.adoc"), "<.> Pyronaut both {0}");
        Files.writeString(callouts.resolve("callout-jvm-only.adoc"), "<.> JVM only");
        Files.writeString(snippets.resolve("common-both.adoc"), "JVM snippet\ncallout:both[arg0=nested]");
        Files.writeString(snippets.resolve("common-both-pyronaut.adoc"), "Pyronaut snippet\ncallout:both[arg0=nested]");
        Files.writeString(snippets.resolve("common-jvm-only.adoc"), "JVM only snippet");
        Files.writeString(snippets.resolve("common-license.adoc"), "License");
        Files.writeString(snippets.resolve("common-forced-pyronaut.adoc"), "forced pyronaut snippet");

        Path inputDir = Files.createDirectory(tempDir.resolve("input"));
        Path outputDir = Files.createDirectory(tempDir.resolve("output"));
        Files.writeString(inputDir.resolve("variant-test.adoc"), """
                callout:both[arg0=one]
                callout:jvm-only[]
                callout:both-pyronaut[arg0=explicit]
                common:both.adoc[]
                common:jvm-only.adoc[]
                common:forced-pyronaut.adoc[]
                """);

        GuideAsciidocGenerator.generate(javaAndPythonGuide("variant-test.adoc", "variant-test"), inputDir.toFile(), outputDir.toFile(), projectDir.toFile());

        String javaOutput = Files.readString(outputDir.resolve("variant-test-gradle-java.adoc"));
        String pythonOutput = Files.readString(outputDir.resolve("variant-test-pyronaut-python.adoc"));

        assertEquals("""
                <.> JVM both one
                <.> JVM only
                <.> Pyronaut both explicit
                // Start: src/docs/common/snippets/common-both.adoc
                JVM snippet
                <.> JVM both nested
                // End: src/docs/common/snippets/common-both.adoc
                // Start: src/docs/common/snippets/common-jvm-only.adoc
                JVM only snippet
                // End: src/docs/common/snippets/common-jvm-only.adoc
                // Start: src/docs/common/snippets/common-forced-pyronaut.adoc
                forced pyronaut snippet
                // End: src/docs/common/snippets/common-forced-pyronaut.adoc

                """, javaOutput.substring(0, javaOutput.indexOf("// Start: src/docs/common/snippets/common-license.adoc")));
        assertEquals("""
                <.> Pyronaut both one
                <.> JVM only
                <.> Pyronaut both explicit
                // Start: src/docs/common/snippets/common-both-pyronaut.adoc
                Pyronaut snippet
                <.> Pyronaut both nested
                // End: src/docs/common/snippets/common-both-pyronaut.adoc
                // Start: src/docs/common/snippets/common-jvm-only.adoc
                JVM only snippet
                // End: src/docs/common/snippets/common-jvm-only.adoc
                // Start: src/docs/common/snippets/common-forced-pyronaut.adoc
                forced pyronaut snippet
                // End: src/docs/common/snippets/common-forced-pyronaut.adoc

                """, pythonOutput.substring(0, pythonOutput.indexOf("// Start: src/docs/common/snippets/common-license.adoc")));
    }

    private static Guide javaAndPythonGuide(String asciidoctor, String slug) {
        return new Guide(
                "Micronaut application guide",
                "Description for a Micronaut application.",
                List.of("Author"),
                List.of("Getting Started"),
                LocalDate.of(2026, 1, 1),
                null,
                null,
                null,
                false,
                false,
                asciidoctor,
                List.of(Language.JAVA, Language.PYTHON),
                List.of(),
                List.of(BuildTool.GRADLE, BuildTool.PYRONAUT),
                null,
                List.of(),
                slug,
                true,
                null,
                Map.of(),
                List.of(new App(
                        "default",
                        null,
                        ApplicationType.DEFAULT,
                        "Micronaut",
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        List.of(),
                        null,
                        null,
                        null,
                        false,
                        null
                ))
        );
    }
}
