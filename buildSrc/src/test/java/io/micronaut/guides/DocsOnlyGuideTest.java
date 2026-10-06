package io.micronaut.guides;

import io.micronaut.guides.core.App;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideParser;
import io.micronaut.guides.tasks.SampleProjectGenerationTask;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class DocsOnlyGuideTest {

    @Inject GuideParser guideParser;
    @Inject Validator validator;
    @Inject io.micronaut.guides.core.TestScriptGenerator coreScripts;
    @TempDir Path tempDir;

    @Test
    void explicitEmptyAppsAreValidMetadata() throws Exception {
        writeMetadata("docs-only", "[]");
        Guide guide = guideParser.parseGuideMetadata(tempDir.resolve("guides/docs-only").toFile(), "metadata.json").orElseThrow();
        assertTrue(guide.apps().isEmpty());
        assertTrue(validator.validate(guide).isEmpty(), () -> validator.validate(guide).toString());
        assertFalse(guide.skipPyronautTests());
    }

    @Test
    void emptyAppsProduceNoProjectOutputDirectories() throws Exception {
        Guide guide = guide(List.of());
        assertAll(
                () -> assertEquals(List.of(), SampleProjectGenerationTask.outputDirectoryNames(guide, null)),
                () -> assertEquals(List.of(), SampleProjectGenerationTask.outputDirectoryNames(guide, Language.PYTHON))
        );
        try (GuideProjectGenerator generator = new GuideProjectGenerator()) {
            generator.generateOne(guide, tempDir.toFile(), tempDir.resolve("code").toFile());
        }
        try (var paths = Files.list(tempDir.resolve("code"))) {
            assertEquals(0, paths.count());
        }
    }

    @Test
    void aggregateScriptsDoNotEnterDocsOnlyProjectDirectories() {
        Guide guide = guide(List.of());
        assertAll(
                () -> assertFalse(coreScripts.generateTestScript(new ArrayList<>(List.of(guide))).contains("cd docs-only-")),
                () -> assertFalse(coreScripts.generateNativeTestScript(new ArrayList<>(List.of(guide))).contains("cd docs-only-")),
                () -> assertFalse(coreScripts.generatePythonTestScript(new ArrayList<>(List.of(guide))).contains("cd docs-only-")),
                () -> assertFalse(TestScriptGenerator.generateScript(new ArrayList<>(List.of(guide)), false, false, false, null).contains("cd docs-only-")),
                () -> assertFalse(TestScriptGenerator.generateScript(new ArrayList<>(List.of(guide)), false, true, false, null).contains("cd docs-only-")),
                () -> assertFalse(TestScriptGenerator.generateScript(new ArrayList<>(List.of(guide)), false, false, true, null).contains("cd docs-only-"))
        );
    }

    @Test
    void ordinaryAppsStillHaveOutputDirectoriesAndTestCommands() {
        Guide guide = guide(List.of(new App("default", "example.micronaut", ApplicationType.DEFAULT, "Micronaut",
                List.of(), List.of(), List.of(), List.of(), List.of(), null, null, null, false)));
        assertEquals(List.of("docs-only-pyronaut-python"), SampleProjectGenerationTask.outputDirectoryNames(guide, Language.PYTHON));
        assertTrue(coreScripts.generatePythonTestScript(new ArrayList<>(List.of(guide))).contains("cd docs-only-pyronaut-python"));
        assertTrue(TestScriptGenerator.generateScript(new ArrayList<>(List.of(guide)), false, false, true, null).contains("cd docs-only-pyronaut-python"));
    }

    @Test
    void docsOnlyTaskRegistrationRetainsDocsIndexAndBuildWithoutApplicationTasks() throws Exception {
        writeMetadata("docs-only", "[]");
        writeMetadata("ordinary", "[{\"name\":\"default\"}]");
        Files.writeString(tempDir.resolve("guides/tests.properties"), "numberOfTestGroups=1\n");
        Project project = ProjectBuilder.builder().withProjectDir(tempDir.toFile()).build();
        project.getExtensions().getExtraProperties().set("metadataConfigName", "metadata.json");
        for (String name : List.of("asciidoctor", "themeGuides", "createDist", "generateTestScript", "generateGuidesIndex", "generateGuidesJsonMetadata")) {
            project.getTasks().register(name);
        }
        new GuidesPlugin().apply(project);

        assertAll(
                () -> assertNotNull(project.getTasks().findByName("docsOnlyGenerateDocs")),
                () -> assertNotNull(project.getTasks().findByName("docsOnlyGenerateDocsJava")),
                () -> assertNotNull(project.getTasks().findByName("docsOnlyGenerateDocsPython")),
                () -> assertNotNull(project.getTasks().findByName("docsOnlyIndex")),
                () -> assertNotNull(project.getTasks().findByName("docsOnlyBuild")),
                () -> assertNotNull(project.getTasks().findByName("docsOnlyBuildJava")),
                () -> assertNotNull(project.getTasks().findByName("docsOnlyBuildPython")),
                () -> assertFalse(project.getTasks().getNames().stream().anyMatch(name -> name.startsWith("docsOnly")
                        && (name.contains("Zip") || name.contains("TestScript") || name.contains("Workflow")))),
                () -> assertNotNull(project.getTasks().findByName("ordinaryPythonZipCode")),
                () -> assertNotNull(project.getTasks().findByName("ordinaryRunPythonTestScript")),
                () -> assertNotNull(project.getTasks().findByName("ordinaryGenerateGithubActionWorkflow"))
        );
    }

    private void writeMetadata(String slug, String apps) throws Exception {
        Path directory = tempDir.resolve("guides/" + slug);
        Files.createDirectories(directory);
        Files.writeString(directory.resolve("metadata.json"), """
                {
                  "title": "Documentation guide",
                  "intro": "Connect the existing application.",
                  "authors": ["Micronaut"],
                  "categories": ["Data JDBC"],
                  "publicationDate": "2026-10-06",
                  "languages": ["JAVA", "PYTHON"],
                  "apps": %s
                }
                """.formatted(apps));
    }

    private static Guide guide(List<App> apps) {
        return new Guide("Documentation guide", "Connect the existing application.", List.of("Micronaut"),
                List.of("Data JDBC"), LocalDate.of(2026, 10, 6), null, null, null, false, false,
                "docs-only.adoc", List.of(Language.JAVA, Language.PYTHON), List.of(),
                List.of(BuildTool.GRADLE, BuildTool.MAVEN, BuildTool.PYRONAUT), TestFramework.JUNIT,
                List.of(), "docs-only", true, null, Map.of(), apps, false);
    }
}
