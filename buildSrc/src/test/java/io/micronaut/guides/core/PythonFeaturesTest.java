package io.micronaut.guides.core;

import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class PythonFeaturesTest {
    @Inject
    GuideParser guideParser;

    @TempDir
    Path directory;

    @Test
    void publishedPythonFeaturesApplyOnlyToPython() throws IOException {
        Guide guide = parse("python-features", "", """
                "features": ["data-mongodb"],
                "invisibleFeatures": ["serialization-jackson"],
                "jvmFeatures": ["reactor"],
                "javaFeatures": ["graalvm"],
                "kotlinFeatures": ["kapt"],
                "groovyFeatures": ["groovy-toml"],
                "pythonFeatures": ["validation"]
                """);
        App app = guide.apps().get(0);

        assertEquals(List.of("data-mongodb", "reactor", "graalvm"), GuideUtils.getAppVisibleFeatures(app, Language.JAVA));
        assertEquals(List.of("data-mongodb", "reactor", "kapt"), GuideUtils.getAppVisibleFeatures(app, Language.KOTLIN));
        assertEquals(List.of("data-mongodb", "reactor", "groovy-toml"), GuideUtils.getAppVisibleFeatures(app, Language.GROOVY));
        assertEquals(List.of("data-mongodb", "serialization-jackson", "reactor", "graalvm"), GuideUtils.getAppFeatures(app, Language.JAVA));
        assertEquals(List.of("data-mongodb", "serialization-jackson", "reactor", "kapt"), GuideUtils.getAppFeatures(app, Language.KOTLIN));
        assertEquals(List.of("data-mongodb", "serialization-jackson", "reactor", "groovy-toml"), GuideUtils.getAppFeatures(app, Language.GROOVY));
        assertEquals(List.of("data-mongodb", "validation"), GuideUtils.getAppVisibleFeatures(app, Language.PYTHON));
        assertEquals(List.of("data-mongodb", "serialization-jackson", "validation"), GuideUtils.getAppFeatures(app, Language.PYTHON));
    }

    @Test
    void pythonFeaturesContributeTags() throws IOException {
        Guide guide = parse("python-tags", "", "\"pythonFeatures\": [\"micronaut-validation\"]");
        assertTrue(GuideUtils.getTags(guide).contains("validation"));
    }

    @Test
    void baseAndChildPythonFeaturesMergeWithoutLeakingJvmFeatures() throws IOException {
        parse("a-base", "", """
                "features": ["data-mongodb"],
                "invisibleFeatures": ["serialization-jackson"],
                "jvmFeatures": ["reactor"],
                "pythonFeatures": ["validation"]
                """);
        parse("child", "\"base\": \"a-base\",", """
                "features": ["http-client"],
                "pythonFeatures": ["security"]
                """);

        Guide guide = guideParser.parseGuidesMetadata(directory.toFile(), "metadata.json")
                .stream().filter(candidate -> candidate.slug().equals("child")).findFirst().orElseThrow();
        App app = guide.apps().get(0);
        assertEquals(List.of("http-client", "data-mongodb", "reactor"), GuideUtils.getAppVisibleFeatures(app, Language.JAVA));
        assertEquals(List.of("http-client", "data-mongodb", "security", "validation"), GuideUtils.getAppVisibleFeatures(app, Language.PYTHON));
        assertEquals(List.of("http-client", "data-mongodb", "serialization-jackson", "security", "validation"), GuideUtils.getAppFeatures(app, Language.PYTHON));
        assertTrue(GuideUtils.getTags(guide).containsAll(List.of("security", "validation")));
    }

    @Test
    void publishedPythonFeaturesMustBeAnArrayOfStrings() throws IOException {
        Path guideDirectory = write("invalid-python-features", "", "\"pythonFeatures\": \"validation\"");
        assertTrue(guideParser.parseGuideMetadata(guideDirectory.toFile(), "metadata.json").isEmpty());
    }

    @Test
    void existingAppConstructorRetainsSharedAndJvmFeatureBehavior() {
        App app = new App("default", "example.micronaut", null, "Micronaut",
                List.of("data-mongodb"), List.of("serialization-jackson"),
                List.of(), List.of(), List.of(), List.of("reactor"), null, null, null, false);
        assertEquals(List.of("data-mongodb"), GuideUtils.getAppVisibleFeatures(app, Language.PYTHON));
        assertEquals(List.of("data-mongodb", "serialization-jackson"), GuideUtils.getAppFeatures(app, Language.PYTHON));
        assertEquals(List.of("data-mongodb", "serialization-jackson", "reactor"), GuideUtils.getAppFeatures(app, Language.JAVA));
    }

    private Guide parse(String slug, String guideProperties, String appProperties) throws IOException {
        Path guideDirectory = write(slug, guideProperties, appProperties);
        return guideParser.parseGuideMetadata(guideDirectory.toFile(), "metadata.json").orElseThrow();
    }

    private Path write(String slug, String guideProperties, String appProperties) throws IOException {
        Path guideDirectory = Files.createDirectories(directory.resolve(slug));
        Files.writeString(guideDirectory.resolve("metadata.json"), """
                {
                  "title": "Python features",
                  "intro": "Select features by language.",
                  "authors": ["Micronaut"],
                  "categories": ["Data JDBC"],
                  "publicationDate": "2026-10-07",
                  "languages": ["JAVA", "GROOVY", "KOTLIN", "PYTHON"],
                  %s
                  "apps": [{"name": "default", "validateLicense": false, %s}]
                }
                """.formatted(guideProperties, appProperties));
        return guideDirectory;
    }
}
