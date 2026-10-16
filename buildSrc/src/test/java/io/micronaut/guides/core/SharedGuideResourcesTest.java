package io.micronaut.guides.core;

import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(startApplication = false)
class SharedGuideResourcesTest {
    private static final Logger LOG = LoggerFactory.getLogger(SharedGuideResourcesTest.class);

    @Inject
    FilesTransferUtility filesTransferUtility;

    @Inject
    GuideParser guideParser;

    @TempDir
    Path temporaryDirectory;

    @Test
    void sharesMainAndTestResourcesUsingEachLanguagesLayout() throws IOException {
        Guide guide = guide("shared-resources", null, "default");
        write(source(guide, "default").resolve("src/main/resources/data.txt"), "shared main");
        write(source(guide, "default").resolve("src/test/resources/data.json"), "shared test");
        for (GuidesOption option : GuideGenerationUtils.guidesOptions(guide, LOG)) {
            if (option.getLanguage() == Language.PYTHON) {
                write(project(guide, option, "default").resolve("src/main/resources/data.txt"), "stale main");
                write(project(guide, option, "default").resolve("src/test/resources/data.json"), "stale test");
            }
        }

        transfer(guide, "default");

        for (GuidesOption option : GuideGenerationUtils.guidesOptions(guide, LOG)) {
            Path project = project(guide, option, "default");
            boolean python = option.getLanguage() == Language.PYTHON;
            assertEquals("shared main", Files.readString(project.resolve(python ? "config/data.txt" : "src/main/resources/data.txt")));
            assertEquals("shared test", Files.readString(project.resolve(python ? "tests-config/data.json" : "src/test/resources/data.json")));
            if (python) {
                assertFalse(Files.exists(project.resolve("src/main/resources/data.txt")));
                assertFalse(Files.exists(project.resolve("src/test/resources/data.json")));
            }
        }
    }

    @Test
    void sharesJvmConfigurationWithoutCopyingItIntoPython() throws IOException {
        Guide guide = guide("jvm-resources", null, "default");
        Path source = source(guide, "default");
        write(source.resolve("jvm/src/main/resources/application.properties"), "jvm configuration");
        write(source.resolve("jvm/src/main/resources/logback.xml"), "jvm logging");
        write(source.resolve("python/config/application.toml"), "python configuration");

        transfer(guide, "default");

        for (GuidesOption option : GuideGenerationUtils.guidesOptions(guide, LOG)) {
            Path project = project(guide, option, "default");
            if (option.getLanguage() == Language.PYTHON) {
                assertEquals("python configuration", Files.readString(project.resolve("config/application.toml")));
                assertFalse(Files.exists(project.resolve("config/application.properties")));
                assertFalse(Files.exists(project.resolve("config/logback.xml")));
            } else {
                assertEquals("jvm configuration", Files.readString(project.resolve("src/main/resources/application.properties")));
                assertEquals("jvm logging", Files.readString(project.resolve("src/main/resources/logback.xml")));
            }
            assertFalse(Files.exists(project.resolve("jvm")));
        }
    }

    @Test
    void languageSpecificFilesOverrideSharedAndJvmFiles() throws IOException {
        Guide guide = guide("resource-overrides", null, "default");
        Path source = source(guide, "default");
        write(source.resolve("src/main/resources/application.properties"), "shared");
        write(source.resolve("jvm/src/main/resources/application.properties"), "jvm");
        write(source.resolve("src/main/resources/layer.txt"), "shared");
        write(source.resolve("jvm/src/main/resources/layer.txt"), "jvm");
        for (String language : new String[]{"java", "groovy", "kotlin"}) {
            write(source.resolve(language).resolve("src/main/resources/application.properties"), language);
        }
        write(source.resolve("python/config/application.properties"), "python");

        transfer(guide, "default");

        for (GuidesOption option : GuideGenerationUtils.guidesOptions(guide, LOG)) {
            boolean python = option.getLanguage() == Language.PYTHON;
            String resource = python ? "config/application.properties" : "src/main/resources/application.properties";
            assertEquals(option.getLanguage().toString(), Files.readString(project(guide, option, "default").resolve(resource)));
            String layer = python ? "config/layer.txt" : "src/main/resources/layer.txt";
            assertEquals(python ? "shared" : "jvm", Files.readString(project(guide, option, "default").resolve(layer)));
        }
    }

    @Test
    void appliesBaseAndChildResourceLayersToNamedApplications() throws IOException {
        Guide base = guide("resource-base", null, "api");
        Guide child = guide("resource-child", base.slug(), "api");
        write(source(base, "api").resolve("src/test/resources/data.json"), "base data");
        write(source(base, "api").resolve("jvm/src/main/resources/application.properties"), "base jvm");
        write(source(base, "api").resolve("jvm/src/main/resources/logback.xml"), "base logging");
        write(source(child, "api").resolve("src/test/resources/data.json"), "child data");
        write(source(child, "api").resolve("jvm/src/main/resources/application.properties"), "child jvm");

        transfer(child, "api");

        for (GuidesOption option : GuideGenerationUtils.guidesOptions(child, LOG)) {
            Path project = project(child, option, "api");
            if (option.getLanguage() == Language.PYTHON) {
                assertEquals("child data", Files.readString(project.resolve("tests-config/data.json")));
                assertFalse(Files.exists(project.resolve("config/application.properties")));
                assertFalse(Files.exists(project.resolve("config/logback.xml")));
            } else {
                assertEquals("child data", Files.readString(project.resolve("src/test/resources/data.json")));
                assertEquals("child jvm", Files.readString(project.resolve("src/main/resources/application.properties")));
                assertEquals("base logging", Files.readString(project.resolve("src/main/resources/logback.xml")));
            }
        }
    }

    private Guide guide(String slug, String base, String app) throws IOException {
        Path directory = temporaryDirectory.resolve(slug);
        write(directory.resolve("metadata.json"), """
                {
                  "title": "Shared resources", "intro": "Resource copying regression.",
                  "authors": ["Micronaut"], "categories": ["Getting Started"],
                  "publicationDate": "2026-01-01",
                  "languages": ["JAVA", "GROOVY", "KOTLIN", "PYTHON"],
                  "buildTools": ["GRADLE", "MAVEN"],
                  %s
                  "apps": [{"name": "%s"}]
                }
                """.formatted(base == null ? "" : "\"base\": \"" + base + "\",", app));
        return guideParser.parseGuideMetadata(directory.toFile(), "metadata.json").orElseThrow();
    }

    private Path source(Guide guide, String app) {
        return temporaryDirectory.resolve(guide.slug()).resolve("default".equals(app) ? "" : app);
    }

    private Path project(Guide guide, GuidesOption option, String app) {
        return temporaryDirectory.resolve("generated").resolve(MacroUtils.getSourceDir(guide.slug(), option))
                .resolve("default".equals(app) ? "" : app);
    }

    private void transfer(Guide guide, String app) throws IOException {
        for (GuidesOption option : GuideGenerationUtils.guidesOptions(guide, LOG)) {
            Files.createDirectories(project(guide, option, app).resolve("src"));
        }
        filesTransferUtility.transferFiles(temporaryDirectory.resolve(guide.slug()).toFile(),
                temporaryDirectory.resolve("generated").toFile(), guide);
    }

    private static void write(Path file, String content) throws IOException {
        Files.createDirectories(file.getParent());
        Files.writeString(file, content);
    }
}
