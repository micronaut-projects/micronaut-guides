package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.micronaut.starter.options.BuildTool.GRADLE;
import static io.micronaut.starter.options.BuildTool.MAVEN;
import static io.micronaut.starter.options.BuildTool.PYRONAUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class TestScriptGeneratorTest {

    @Inject
    GuideParser guideParser;

    @Inject
    TestScriptGenerator testScriptGenerator;

    @Inject
    ResourceLoader resourceLoader;

    @Test
    void supportsNativeTestAllConditions() {
        App app = new App("springboot", null, null, null, null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertTrue(result);
    }

    @Test
    void supportsNativeTestNotMicronaut() {
        App app = new App("app", null, null, "Spring", null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotGradle() {
        App app = new App("springboot", null, null, "Micronaut", null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(MAVEN, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotLanguage() {
        App app = new App("springboot", null, null, "Micronaut", null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.GROOVY, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotJUnit() {
        App app = new App("springboot", null, null, "Micronaut", null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.SPOCK);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNullFramework() {
        App app = new App("springboot", null, null, null, null, null, null, null, null, null, null, null, false);

        assertTrue(testScriptGenerator.isMicronautFramework(app));
    }

    @Test
    void supportsNativeTestIsMicronaut() {
        App app = new App("springboot", null, null, "Micronaut", null, null, null, null, null, null, null, null, false);

        assertTrue(testScriptGenerator.isMicronautFramework(app));
    }

    @Test
    void supportsNativeTestIsNotMicronaut() {
        App app = new App("app", null, null, "Spring", null, null, null, null, null, null, null, null, false);

        assertFalse(testScriptGenerator.isMicronautFramework(app));
    }

    @Test
    void supportsNativeTestIsGroovy() {
        assertFalse(testScriptGenerator.supportsNativeTest(Language.GROOVY));
    }

    @Test
    void testGenerate() {
        String path = "src/test/resources/guides";
        File file = new File(path);
        List<Guide> metadatas = guideParser.parseGuidesMetadata(file, "metadata.json");

        File expectedFile = new File(resourceLoader.getResource("classpath:expected_test_script.sh").orElseThrow().getFile());
        String expected = TestUtils.readFile(expectedFile);

        String result = testScriptGenerator.generateTestScript(metadatas);

        assertEquals(expected.strip(), result.strip());
    }

    @Test
    void testGeneratePython() {
        App app = new App("default", null, null, null, null, null, null, null, null, null, null, null, true);
        Guide guide = new Guide(
                "Python guide",
                "Tests Python guide script generation.",
                List.of("Micronaut"),
                List.of("Getting Started"),
                java.time.LocalDate.of(2026, 5, 13),
                null,
                null,
                null,
                false,
                false,
                null,
                List.of(Language.PYTHON),
                List.of(),
                List.of(PYRONAUT),
                TestFramework.PYTEST,
                List.of(),
                "python-guide",
                true,
                null,
                java.util.Map.of(),
                List.of(app),
                true,
                false
        );

        String result = testScriptGenerator.generatePythonTestScript(new ArrayList<>(List.of(guide)));

        assertTrue(result.contains("PYRONAUT_LOCAL_REPOSITORY"));
        assertTrue(result.contains("PYRONAUT_LOCAL_CORE_VERSION"));
        assertTrue(result.contains("PYRONAUT_LOCAL_PLATFORM_VERSION"));
        assertTrue(result.contains("PYRONAUT_INSTALL_EXECUTABLE"));
        assertTrue(result.contains("PYRONAUT_REFRESH_DEPENDENCIES"));
        assertTrue(result.contains("PYRONAUT_DEPENDENCY_CACHE:-.pyronaut-m2"));
        assertTrue(result.contains("PYRONAUT_PROCESS_EXECUTABLE"));
        assertTrue(result.contains("PYRONAUT_CLI_PYTHONPATH"));
        assertTrue(result.contains("set_pyronaut_local_versions"));
        assertTrue(result.contains("[tool.pyronaut.core]"));
        assertTrue(result.contains("[tool.pyronaut.platform]"));
        assertFalse(result.contains("PYRONAUT_LOCAL_VERSION"));
        assertTrue(result.contains("run_pyronaut_install"));
        assertTrue(result.contains("run_pyronaut_process"));
        assertTrue(result.contains("run_pyronaut_cli"));
        assertTrue(result.contains("test_args+=(--local-repository \"$(pyronaut_dependency_cache)\")"));
        assertTrue(result.contains("run_pyronaut_cli test \"${test_args[@]}\""));
        assertFalse(result.contains("\"$PYRONAUT_TEST_EXECUTABLE\""));
        assertTrue(result.contains("set_pyronaut_local_repositories"));
        assertTrue(result.contains("cd python-guide-pyronaut-python"));
        assertFalse(result.contains("}cd python-guide-pyronaut-python"));
        assertTrue(result.contains("run_pyronaut_tests || EXIT_STATUS=$?"));
        assertTrue(result.contains("tar --exclude='./.micronaut'"));
        assertFalse(result.contains("./gradlew -q check"));
    }

    @Test
    void testGenerateNative() {
        String path = "src/test/resources/guides";
        File file = new File(path);
        List<Guide> metadatas = guideParser.parseGuidesMetadata(file, "metadata.json");

        File expectedFile = new File(resourceLoader.getResource("classpath:expected_test_script_native.sh").orElseThrow().getFile());
        String expected = TestUtils.readFile(expectedFile);

        String result = testScriptGenerator.generateNativeTestScript(metadatas);

        assertEquals(expected.strip(), result.strip());
    }
}
