package io.micronaut.guides.feature;

import io.micronaut.guides.core.App;
import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideProjectGenerator;
import io.micronaut.guides.core.GuidesOption;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class TestResourcesLongTimeoutTest {

    @Inject
    GuideProjectGenerator guideProjectGenerator;

    @Test
    void pyronautProjectsGetTheClientTimeout(@TempDir File outputDirectory) throws Exception {
        Guide guide = guide(List.of(Language.PYTHON), List.of(BuildTool.PYRONAUT), TestFramework.PYTEST);

        guideProjectGenerator.generate(outputDirectory, guide);

        String pyproject = read(outputDirectory, guide, new GuidesOption(BuildTool.PYRONAUT, Language.PYTHON, TestFramework.PYTEST), "pyproject.toml");
        int table = pyproject.indexOf("[tool.pyronaut.test-resources]\n");
        assertTrue(table >= 0, pyproject);
        String tableBody = pyproject.substring(table, nextTable(pyproject, table));
        assertTrue(tableBody.contains("\nclient-timeout = 600\n"), tableBody);
        assertTrue(tableBody.contains("\nenabled = true\n"), tableBody);
        assertEquals(1, pyproject.split("client-timeout = 600", -1).length - 1, pyproject);
    }

    @Test
    void gradleProjectsKeepTheClientTimeout(@TempDir File outputDirectory) throws Exception {
        Guide guide = guide(List.of(Language.JAVA), List.of(BuildTool.GRADLE), TestFramework.JUNIT);

        guideProjectGenerator.generate(outputDirectory, guide);

        String buildGradle = read(outputDirectory, guide, new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT), "build.gradle");
        assertTrue(buildGradle.contains("clientTimeout = 600"), buildGradle);
    }

    @Test
    void clientTimeoutIsInsertedIntoAnExistingTestResourcesTable() {
        String pyproject = """
            [tool.pyronaut.test-resources]
            enabled = true
            infer-classpath = true

            [tool.pyronaut.dependencies]
            runtime = []
            """;

        String result = TestResourcesLongTimeout.PyronautClientTimeout.withClientTimeout(pyproject);

        assertEquals("""
            [tool.pyronaut.test-resources]
            client-timeout = 600
            enabled = true
            infer-classpath = true

            [tool.pyronaut.dependencies]
            runtime = []
            """, result);
        assertEquals(result, TestResourcesLongTimeout.PyronautClientTimeout.withClientTimeout(result));
    }

    @Test
    void clientTimeoutTableIsAppendedWhenMissing() {
        String pyproject = """
            [tool.pyronaut]
            repositories = []
            """;

        String result = TestResourcesLongTimeout.PyronautClientTimeout.withClientTimeout(pyproject);

        assertEquals("""
            [tool.pyronaut]
            repositories = []

            [tool.pyronaut.test-resources]
            client-timeout = 600
            """, result);
    }

    private static int nextTable(String pyproject, int from) {
        int next = pyproject.indexOf("\n[", from + 1);
        return next < 0 ? pyproject.length() : next + 1;
    }

    private static String read(File outputDirectory, Guide guide, GuidesOption option, String file) throws IOException {
        Path path = outputDirectory.toPath().resolve(guide.slug() + "-" + option.getBuildTool() + "-" + option.getLanguage()).resolve(file);
        assertTrue(Files.exists(path), path.toString());
        return Files.readString(path);
    }

    private static Guide guide(List<Language> languages, List<BuildTool> buildTools, TestFramework testFramework) {
        App app = new App(
                "default",
                "example.micronaut",
                ApplicationType.DEFAULT,
                "Micronaut",
                List.of("data-jdbc", "oracle"),
                List.of("test-resources-long-timeout"),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                null,
                null,
                null,
                true,
                null
        );
        return new Guide(
                "Oracle test resources",
                "A guide whose tests pull an Oracle image through Test Resources.",
                List.of("Nemanja Mikic"),
                List.of("Data JDBC"),
                LocalDate.of(2026, 10, 31),
                null,
                null,
                null,
                false,
                false,
                "micronaut-data-oracle-timeout.adoc",
                languages,
                List.of("oracle"),
                buildTools,
                testFramework,
                List.of(),
                "micronaut-data-oracle-timeout",
                true,
                null,
                Map.of(),
                List.of(app)
        );
    }
}
