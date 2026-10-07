package io.micronaut.guides;

import io.micronaut.guides.tasks.AsciidocGenerationTask;
import io.micronaut.guides.tasks.TestScriptRunnerTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GuidesPluginTest {

    @TempDir
    Path directory;

    @Test
    void rendererIsOrderedAfterAllLanguageDocumentationTasks() throws IOException {
        Project project = project();
        Task renderer = project.getTasks().getByName("asciidoctor");

        assertAll(List.of("Java", "Groovy", "Kotlin", "Python").stream()
                .map(language -> (Executable) () -> {
                    Task documentation = project.getTasks().getByName("helloGenerateDocs" + language);
                    assertTrue(renderer.getMustRunAfter().getDependencies(renderer).contains(documentation),
                            "Renderer must run after " + documentation.getName());
                }));
    }

    @Test
    void orderingDoesNotForceDocumentationGeneration() throws IOException {
        Project project = project();
        Task renderer = project.getTasks().getByName("asciidoctor");
        Task documentation = project.getTasks().getByName("helloGenerateDocs");

        assertTrue(renderer.getMustRunAfter().getDependencies(renderer).contains(documentation));
        assertTrue(renderer.getTaskDependencies().getDependencies(renderer).isEmpty(),
                "Rendering existing documents must not schedule guide generation");
    }

    @Test
    void orderingIncludesDocumentationTasksRegisteredLater() throws IOException {
        Project project = project();
        Task renderer = project.getTasks().getByName("asciidoctor");
        Task documentation = project.getTasks().register("additionalDocs", AsciidocGenerationTask.class).get();

        assertTrue(renderer.getMustRunAfter().getDependencies(renderer).contains(documentation));
        assertTrue(renderer.getTaskDependencies().getDependencies(renderer).isEmpty());
    }

    @Test
    void releasedPythonRunnerDoesNotRequireLocalCheckouts() throws IOException {
        assertPythonRunner(project(false), true);
    }

    @Test
    void skippedPythonRunnerDoesNotRequireRuntimeSetup() throws IOException {
        assertPythonRunner(project(true), false);
    }

    private void assertPythonRunner(Project project, boolean enabled) {
        TestScriptRunnerTask runner = (TestScriptRunnerTask) project.getTasks().getByName("helloRunPythonTestScript");
        assertAll(
                () -> assertFalse(runner.getTaskDependencies().getDependencies(runner).stream()
                        .anyMatch(task -> task.getName().equals("stageLocalPyronautArtifacts"))),
                () -> assertEquals(Map.of("GUIDE_MODE", "fixture"), runner.getEnvironment().get()),
                () -> assertEquals(enabled, runner.getOnlyIf().isSatisfiedBy(runner))
        );
    }

    private Project project() throws IOException {
        return project(false);
    }

    private Project project(boolean skipPythonTests) throws IOException {
        Path guide = Files.createDirectories(directory.resolve("guides/hello"));
        Files.writeString(directory.resolve("guides/tests.properties"), "numberOfTestGroups=1\n");
        Files.writeString(guide.resolve("metadata.json"), """
                {
                  "title": "Hello",
                  "intro": "A task-wiring fixture",
                  "authors": ["Micronaut"],
                  "categories": ["Core"],
                  "publicationDate": "2026-10-05",
                  "skipPyronautTests": %s,
                  "env": {"GUIDE_MODE": "fixture"},
                  "languages": ["JAVA", "GROOVY", "KOTLIN", "PYTHON"],
                  "apps": [{"name": "default", "features": []}]
                }
                """.formatted(skipPythonTests));
        Project project = ProjectBuilder.builder().withProjectDir(directory.toFile()).build();
        project.getExtensions().getExtraProperties().set("metadataConfigName", "metadata.json");
        project.getTasks().register("asciidoctor");
        project.getPluginManager().apply(GuidesPlugin.class);
        return project;
    }
}
