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
                
                :only-for-languages:python
                Python only
                source:HelloController[]
                test:HelloControllerTest[]
                :only-for-languages:
                
                After
                """);

        Guide guide = new Guide(
                "Macro test",
                "Tests language macros",
                List.of("Author"),
                List.of("Getting Started"),
                LocalDate.of(2026, 1, 1),
                null,
                null,
                null,
                false,
                false,
                "macro-test.adoc",
                List.of(Language.JAVA, Language.PYTHON),
                List.of(),
                List.of(BuildTool.GRADLE, BuildTool.PYRONAUT),
                null,
                List.of(),
                "macro-test",
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
                        null,
                        null,
                        null,
                        false
                ))
        );

        GuideAsciidocGenerator.generate(guide, inputDir.toFile(), outputDir.toFile(), Path.of("..").toAbsolutePath().toFile());

        String javaOutput = Files.readString(outputDir.resolve("macro-test-gradle-java.adoc"));
        String pythonOutput = Files.readString(outputDir.resolve("macro-test-pyronaut-python.adoc"));

        assertFalse(javaOutput.contains("Python only"));
        assertFalse(javaOutput.contains(":only-for-languages:"));
        assertTrue(pythonOutput.contains("Python only"));
        assertFalse(pythonOutput.contains(":only-for-languages:"));
        assertTrue(pythonOutput.contains("src/example/micronaut/hello_controller.py"));
        assertTrue(pythonOutput.contains("tests/example/micronaut/test_hello_controller.py"));
    }
}
