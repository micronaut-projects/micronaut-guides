package io.micronaut.guides.tasks;

import io.micronaut.guides.GuideProjectGenerator;
import io.micronaut.guides.core.App;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.App;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SampleProjectGenerationTaskTest {

    @TempDir
    Path directory;

    @Test
    void outputDirectoriesAreLimitedToTheRequestedLanguage() {
        Guide metadata = new Guide(null, null, null, null, null, null, null, null,
                false, false, null, List.of(Language.JAVA, Language.PYTHON), null,
                List.of(BuildTool.GRADLE, BuildTool.MAVEN), TestFramework.JUNIT,
                null, "multi-language", true, null, null,
                List.of(new App("default", null, null, null, null, null, null, null, null, null, null, null, false)));

        assertEquals(List.of("multi-language-pyronaut-python"),
                SampleProjectGenerationTask.outputDirectoryNames(metadata, Language.PYTHON));
        assertEquals(List.of("multi-language-gradle-java", "multi-language-maven-java",
                        "multi-language-pyronaut-python"),
                SampleProjectGenerationTask.outputDirectoryNames(metadata, null));
    }

    @Test
    void pythonRegenerationRefreshesProjectAndPreservesOtherLanguages() throws IOException {
        Path input = Files.createDirectories(directory.resolve("guide"));
        Path output = directory.resolve("output");
        Path obsoleteSource = write(input.resolve("python/src/obsolete.py"), "obsolete = True\n");
        Guide metadata = metadata(List.of());

        try (GuideProjectGenerator generator = new GuideProjectGenerator()) {
            SampleProjectGenerationTask task = task(metadata, input, output, Language.PYTHON, generator);
            task.perform();
            Path pythonProject = output.resolve("regeneration-pyronaut-python");
            Path pyproject = pythonProject.resolve("pyproject.toml");
            assertFalse(Files.readString(pyproject).contains("io.micronaut.validation:micronaut-validation"));
            assertTrue(Files.exists(pythonProject.resolve("src/obsolete.py")));

            Path javaSentinel = write(output.resolve("regeneration-gradle-java/sentinel.txt"), "Java survives\n");
            Path kotlinSentinel = write(output.resolve("regeneration-gradle-kotlin/sentinel.txt"), "Kotlin survives\n");
            Files.delete(obsoleteSource);
            write(input.resolve("python/src/current.py"), "current = True\n");
            task.setMetadata(metadata(List.of("validation")));

            task.perform();

            String regeneratedPyproject = Files.readString(pyproject);
            assertAll(
                    () -> assertTrue(regeneratedPyproject.contains("io.micronaut.validation:micronaut-validation"),
                            "The changed Starter feature must update pyproject.toml dependencies"),
                    () -> assertFalse(Files.exists(pythonProject.resolve("src/obsolete.py")),
                            "Removed guide samples must not survive regeneration"),
                    () -> assertEquals("current = True\n", Files.readString(pythonProject.resolve("src/current.py"))),
                    () -> assertEquals("Java survives\n", Files.readString(javaSentinel)),
                    () -> assertEquals("Kotlin survives\n", Files.readString(kotlinSentinel)));
        }
    }

    @Test
    void unfilteredGenerationCleansTheEntireOutputDirectory() throws IOException {
        Path input = Files.createDirectories(directory.resolve("guide"));
        Path output = directory.resolve("output");
        Path stalePython = write(output.resolve("regeneration-pyronaut-python/stale.txt"), "stale\n");
        Path staleJava = write(output.resolve("regeneration-gradle-java/stale.txt"), "stale\n");
        Path staleKotlin = write(output.resolve("regeneration-gradle-kotlin/stale.txt"), "stale\n");
        Path undeclaredOutput = write(output.resolve("obsolete-project/stale.txt"), "stale\n");

        try (GuideProjectGenerator generator = new GuideProjectGenerator()) {
            task(metadata(List.of("validation")), input, output, null, generator).perform();

            assertAll(
                    () -> assertFalse(Files.exists(stalePython)),
                    () -> assertFalse(Files.exists(staleJava)),
                    () -> assertFalse(Files.exists(staleKotlin)),
                    () -> assertFalse(Files.exists(undeclaredOutput)),
                    () -> assertTrue(Files.exists(output.resolve("regeneration-pyronaut-python/pyproject.toml"))),
                    () -> assertTrue(Files.readString(output.resolve("regeneration-pyronaut-python/pyproject.toml"))
                            .contains("io.micronaut.validation:micronaut-validation")),
                    () -> assertTrue(Files.exists(output.resolve("regeneration-gradle-java/build.gradle"))),
                    () -> assertTrue(Files.exists(output.resolve("regeneration-gradle-kotlin/build.gradle"))));
        }
    }

    private SampleProjectGenerationTask task(Guide metadata, Path input, Path output, Language language,
                                             GuideProjectGenerator generator) throws IOException {
        Project project = ProjectBuilder.builder()
                .withProjectDir(Files.createDirectories(directory.resolve("gradle-project")).toFile())
                .build();
        SampleProjectGenerationTask task = project.getTasks().create("generateProjects", SampleProjectGenerationTask.class);
        task.setMetadata(metadata);
        task.setGuidesGenerator(generator);
        task.getSlug().set(metadata.slug());
        task.getInputDirectory().set(input.toFile());
        task.getOutputDir().set(output.toFile());
        if (language != null) {
            task.getLanguage().set(language.name());
        }
        task.getOutputDirectories().from(SampleProjectGenerationTask.outputDirectoryNames(metadata, language)
                .stream().map(name -> output.resolve(name).toFile()).toList());
        return task;
    }

    private static Guide metadata(List<String> features) {
        App app = new App("default", "example.micronaut", ApplicationType.DEFAULT, "Micronaut",
                features, List.of(), List.of(), List.of(), List.of(), null, null, null, false);
        return new Guide("Regeneration", "Sample project regeneration", List.of("author"), List.of("Testing"),
                LocalDate.of(2026, 10, 6), null, null, null, false, false, null,
                List.of(Language.JAVA, Language.KOTLIN, Language.PYTHON), List.of(), List.of(BuildTool.GRADLE),
                TestFramework.JUNIT, List.of(), "regeneration", true, null, Map.of(), List.of(app));
    }

    private static Path write(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        return Files.writeString(path, content);
    }
}
