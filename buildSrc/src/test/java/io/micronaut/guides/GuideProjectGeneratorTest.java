package io.micronaut.guides;

import io.micronaut.guides.core.App;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuidesOption;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuideProjectGeneratorTest {

    @TempDir
    Path directory;

    @Test
    void mixedCaseUnderscoreExclusionsRemovePythonSourceAndPackagedAndFlatTests() throws Exception {
        Path project = generatePythonExclusionProject("Hello_Controller", "Hello_ControllerTest");
        GuidesOption option = new GuidesOption(BuildTool.PYRONAUT, Language.PYTHON, TestFramework.PYTEST);
        Method moduleName = GuideProjectGenerator.class.getDeclaredMethod("pythonModuleName", String.class);
        moduleName.setAccessible(true);
        Method testModuleName = GuideProjectGenerator.class.getDeclaredMethod("pythonTestModuleName", String.class);
        testModuleName.setAccessible(true);

        assertAll(
                () -> assertEquals("hello_controller", moduleName.invoke(null, "Hello_Controller")),
                () -> assertEquals("test_hello_controller", testModuleName.invoke(null, "Hello_ControllerTest")),
                () -> assertEquals("src/example/micronaut/hello_controller.py",
                        GuideAsciidocGenerator.mainPath("", "Hello_Controller", option)),
                () -> assertEquals("tests/example/micronaut/test_hello_controller.py",
                        GuideAsciidocGenerator.testPath("", "Hello_ControllerTest", option)),
                () -> assertFalse(Files.exists(project.resolve("src/example/micronaut/hello_controller.py"))),
                () -> assertFalse(Files.exists(project.resolve("tests/example/micronaut/test_hello_controller.py"))),
                () -> assertFalse(Files.exists(project.resolve("tests/test_hello_controller.py")))
        );
    }

    @Test
    void lowercaseExclusionsPreserveAlreadyCorrectPythonModuleNames() throws Exception {
        Path project = generatePythonExclusionProject("hello_controller", "test_hello_controller");
        GuidesOption option = new GuidesOption(BuildTool.PYRONAUT, Language.PYTHON, TestFramework.PYTEST);
        Method moduleName = GuideProjectGenerator.class.getDeclaredMethod("pythonModuleName", String.class);
        moduleName.setAccessible(true);
        Method testModuleName = GuideProjectGenerator.class.getDeclaredMethod("pythonTestModuleName", String.class);
        testModuleName.setAccessible(true);

        assertAll(
                () -> assertEquals("hello_controller", moduleName.invoke(null, "hello_controller")),
                () -> assertEquals("test_hello_controller", testModuleName.invoke(null, "test_hello_controller")),
                () -> assertEquals("src/example/micronaut/hello_controller.py",
                        GuideAsciidocGenerator.mainPath("", "hello_controller", option)),
                () -> assertEquals("tests/example/micronaut/test_hello_controller.py",
                        GuideAsciidocGenerator.testPath("", "test_hello_controller", option)),
                () -> assertFalse(Files.exists(project.resolve("src/example/micronaut/hello_controller.py"))),
                () -> assertFalse(Files.exists(project.resolve("tests/example/micronaut/test_hello_controller.py"))),
                () -> assertFalse(Files.exists(project.resolve("tests/test_hello_controller.py")))
        );
    }

    private Path generatePythonExclusionProject(String sourceIdentifier, String testIdentifier) throws Exception {
        Path input = Files.createDirectory(directory.resolve("guide"));
        for (String path : List.of("src/example/micronaut/hello_controller.py", "src/example/micronaut/retained.py",
                "tests/example/micronaut/test_hello_controller.py", "tests/example/micronaut/test_retained.py",
                "tests/test_hello_controller.py", "tests/test_retained.py")) {
            Path file = input.resolve("python/" + path);
            Files.createDirectories(file.getParent());
            Files.writeString(file, "pass\n");
        }
        App app = new App("default", "example.micronaut", ApplicationType.DEFAULT, "Micronaut",
                List.of(), List.of(), List.of(), List.of(), List.of(),
                TestFramework.PYTEST, List.of(testIdentifier), List.of(sourceIdentifier), false);
        Guide guide = new Guide("Python exclusions", "Exclude Python modules.", List.of("Micronaut"),
                List.of("Getting Started"), LocalDate.of(2026, 10, 6), null, null, null, false, false,
                "python-exclusions.adoc", List.of(Language.PYTHON), List.of(), List.of(BuildTool.PYRONAUT),
                TestFramework.PYTEST, List.of(), "python-exclusions", true, null, Map.of(), List.of(app), false);
        Path output = directory.resolve("output");
        try (GuideProjectGenerator generator = new GuideProjectGenerator()) {
            generator.generateOne(guide, input.toFile(), output.toFile(), Language.PYTHON);
        }
        Path project = output.resolve("python-exclusions-pyronaut-python");
        assertAll(
                () -> assertTrue(Files.exists(project.resolve("pyproject.toml"))),
                () -> assertTrue(Files.exists(project.resolve("src/example/micronaut/retained.py"))),
                () -> assertTrue(Files.exists(project.resolve("tests/example/micronaut/test_retained.py"))),
                () -> assertTrue(Files.exists(project.resolve("tests/test_retained.py")))
        );
        return project;
    }

    @Test
    void pythonResourceTransferDoesNotInheritJvmBootstrapConfiguration() throws Exception {
        File input = new File("src/test/resources/file-transfer/python-application-config");
        Path python = Files.createDirectory(directory.resolve("python"));
        Path java = Files.createDirectory(directory.resolve("java"));
        Method transfer = GuideProjectGenerator.class.getDeclaredMethod("copyGuideSourceFiles",
                File.class, Path.class, String.class, String.class, boolean.class);
        transfer.setAccessible(true);

        transfer.invoke(null, input, python, "", "python", false);
        transfer.invoke(null, input, java, "", "java", false);

        assertTrue(Files.exists(python.resolve("config/application.toml")));
        assertTrue(Files.exists(python.resolve("tests-config/application-test.toml")));
        assertFalse(Files.exists(python.resolve("config/bootstrap.properties")));
        assertFalse(Files.exists(python.resolve("tests-config/bootstrap-test.properties")));
        assertTrue(Files.exists(java.resolve("src/main/resources/bootstrap.properties")));
        assertTrue(Files.exists(java.resolve("src/test/resources/bootstrap-test.properties")));
    }

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
