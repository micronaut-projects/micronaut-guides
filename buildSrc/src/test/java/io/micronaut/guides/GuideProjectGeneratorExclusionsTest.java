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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuideProjectGeneratorExclusionsTest {

    @TempDir
    Path tempDir;

    @Test
    void pythonExclusionsRemoveInheritedSourcesAndTests() throws Exception {
        assertGeneratedExclusions(Language.PYTHON, "default");
    }

    @Test
    void pythonExclusionsRemoveInheritedSourcesAndTestsFromNamedApp() throws Exception {
        assertGeneratedExclusions(Language.PYTHON, "inventory");
    }

    @Test
    void jvmExclusionsPreserveRetainedSourcesAndTests() throws Exception {
        for (Language language : List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN)) {
            assertGeneratedExclusions(language, "default");
        }
    }

    @Test
    void jvmExclusionsPreserveRetainedSourcesAndTestsFromNamedApp() throws Exception {
        for (Language language : List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN)) {
            assertGeneratedExclusions(language, "inventory");
        }
    }

    private void assertGeneratedExclusions(Language language, String appName) throws Exception {
        String slug = "exclusions-" + language + "-" + appName;
        Path inputDir = Files.createDirectory(tempDir.resolve(slug));
        Path baseDir = Files.createDirectory(tempDir.resolve(slug + "-base"));
        Path outputDir = Files.createDirectory(tempDir.resolve(slug + "-output"));
        boolean python = language == Language.PYTHON;
        String extension = "." + language.getExtension();
        String mainFolder = python ? "src/example/micronaut/" : "src/main/" + language + "/example/micronaut/";
        String testFolder = python ? "tests/example/micronaut/" : "src/test/" + language + "/example/micronaut/";
        String controller = python ? "fruit_controller.py" : "FruitController" + extension;
        String service = python ? "fruit_service.py" : "FruitService" + extension;
        String controllerTest = python ? "test_fruit_controller.py" : "FruitController" + (language == Language.GROOVY ? "Spec" : "Test") + extension;
        String serviceTest = python ? "test_fruit_service.py" : "FruitService" + (language == Language.GROOVY ? "Spec" : "Test") + extension;
        String retainedSource = python ? "retained_service.py" : "RetainedService" + extension;
        String retainedTest = python ? "test_retained_service.py" : "RetainedService" + (language == Language.GROOVY ? "Spec" : "Test") + extension;
        String module = appName.equals("default") ? "" : appName + "/";
        Path baseLanguageDir = baseDir.resolve(module + language);
        for (String path : List.of(mainFolder + controller, mainFolder + service, mainFolder + retainedSource,
                testFolder + controllerTest, testFolder + serviceTest, testFolder + retainedTest)) {
            Path file = baseLanguageDir.resolve(path);
            Files.createDirectories(file.getParent());
            Files.writeString(file, python ? "# inherited sample\n" : "// inherited sample\n");
        }
        Files.createDirectories(inputDir.resolve(module + language));

        App app = new App(appName, "example.micronaut", ApplicationType.DEFAULT, "Micronaut",
                List.of("http-client"), List.of(), List.of(), List.of(), List.of(), List.of(), null,
                List.of("FruitControllerTest", "FruitServiceTest"), List.of("FruitController", "FruitService"), false);
        Guide guide = new Guide("Source exclusions", "Excluded inherited samples must not be generated.",
                List.of("Author"), List.of("Data Access"), LocalDate.of(2026, 1, 1), null, null, null,
                false, false, null, List.of(language), List.of(), List.of(BuildTool.GRADLE), null, List.of(),
                slug, true, baseDir.getFileName().toString(), Map.of(), List.of(app));

        try (GuideProjectGenerator generator = new GuideProjectGenerator()) {
            generator.generateOne(guide, inputDir.toFile(), outputDir.toFile());
        }

        Path project = outputDir.resolve(slug + (python ? "-pyronaut-python" : "-gradle-" + language)).resolve(module);
        assertAll(
                () -> assertTrue(Files.exists(project.resolve(python ? "pyproject.toml" : "build.gradle")), "Starter project generated"),
                () -> assertTrue(Files.exists(project.resolve(mainFolder + retainedSource)), "Retained inherited source"),
                () -> assertTrue(Files.exists(project.resolve(testFolder + retainedTest)), "Retained inherited test"),
                () -> assertFalse(Files.exists(project.resolve(mainFolder + controller)), "Excluded inherited controller"),
                () -> assertFalse(Files.exists(project.resolve(mainFolder + service)), "Excluded inherited service"),
                () -> assertFalse(Files.exists(project.resolve(testFolder + controllerTest)), "Excluded inherited controller test"),
                () -> assertFalse(Files.exists(project.resolve(testFolder + serviceTest)), "Excluded inherited service test")
        );
    }
}
