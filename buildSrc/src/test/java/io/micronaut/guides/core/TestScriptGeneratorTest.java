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
        App app = new App("springboot", null, null, null, null, null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertTrue(result);
    }

    @Test
    void supportsNativeTestNotMicronaut() {
        App app = new App("app", null, null, "Spring", null, null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotGradle() {
        App app = new App("springboot", null, null, "Micronaut", null, null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(MAVEN, Language.JAVA, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotLanguage() {
        App app = new App("springboot", null, null, "Micronaut", null, null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.GROOVY, TestFramework.JUNIT);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNotJUnit() {
        App app = new App("springboot", null, null, "Micronaut", null, null, null, null, null, null, null, null, null, false);
        GuidesOption guidesOption = new GuidesOption(GRADLE, Language.JAVA, TestFramework.SPOCK);

        boolean result = testScriptGenerator.supportsNativeTest(app, guidesOption);

        assertFalse(result);
    }

    @Test
    void supportsNativeTestNullFramework() {
        App app = new App("springboot", null, null, null, null, null, null, null, null, null, null, null, null, false);

        assertTrue(testScriptGenerator.isMicronautFramework(app));
    }

    @Test
    void supportsNativeTestIsMicronaut() {
        App app = new App("springboot", null, null, "Micronaut", null, null, null, null, null, null, null, null, null, false);

        assertTrue(testScriptGenerator.isMicronautFramework(app));
    }

    @Test
    void supportsNativeTestIsNotMicronaut() {
        App app = new App("app", null, null, "Spring", null, null, null, null, null, null, null, null, null, false);

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
    void testGenerateNative() {
        String path = "src/test/resources/guides";
        File file = new File(path);
        List<Guide> metadatas = guideParser.parseGuidesMetadata(file, "metadata.json");

        File expectedFile = new File(resourceLoader.getResource("classpath:expected_test_script_native.sh").orElseThrow().getFile());
        String expected = TestUtils.readFile(expectedFile);

        String result = testScriptGenerator.generateNativeTestScript(metadatas);

        assertEquals(expected.strip(), result.strip());
    }

    @Test
    void testGeneratePython() {
        File guideFolder = new File("src/test/resources/file-transfer/python-resources");
        Guide guide = guideParser.parseGuideMetadata(guideFolder, "metadata.json").orElseThrow();

        String result = testScriptGenerator.generateTestScript(new ArrayList<>(List.of(guide)));

        assertTrue(result.contains("cd python-resources-pyronaut-python"));
        assertFalse(result.contains("pyenv"));
        assertTrue(result.contains("pyronaut install"));
        assertTrue(result.contains("pyronaut validate-config"));
        assertTrue(result.contains("pyronaut test"));
        assertFalse(result.contains("./gradlew -q check"));
        assertFalse(result.contains("./mvnw -q test"));
    }
}
