package io.micronaut.guides.core;

import io.micronaut.json.JsonMapper;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.gradle.api.JavaVersion;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
public class GuideProjectGeneratorTest {
    @Inject
    JsonMapper jsonMapper;

    @Inject
    JsonSchemaProvider jsonSchemaProvider;


    @Inject
    GuideProjectGenerator guideProjectGenerator;

    @Test
    void testGenerate() {
        File outputDirectory = new File("build/tmp/test");
        outputDirectory.mkdir();
        App app = new App(
                "cli",
                "example.micronaut",
                ApplicationType.CLI,
                "Micronaut",
                List.of("yaml", "mqtt"),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                null,
                null,
                null,
                true
        );
        Guide guide = new Guide(
                "1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API",
                "This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.",
                List.of("Sergio del Amo"),
                List.of("Boot to Micronaut Building a REST API"),
                LocalDate.of(2024, 4, 24),
                null,
                null,
                null,
                false,
                false,
                "building-a-rest-api-spring-boot-vs-micronaut-data.adoc",
                List.of(Language.JAVA),
                List.of("spring-boot"),
                List.of(BuildTool.GRADLE),
                TestFramework.JUNIT,
                List.of(),
                "building-a-rest-api-spring-boot-vs-micronaut-data",
                true,
                null,
                Map.of(),
                List.of(app)
        );

        assertDoesNotThrow(() -> guideProjectGenerator.generate(outputDirectory, guide));

        File dest = Paths.get(outputDirectory.getAbsolutePath(), MacroUtils.getSourceDir(guide.slug(), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT)), "cli").toFile();

        assertTrue(new File(dest, "build.gradle").exists());
        assertTrue(new File(dest, "gradlew.bat").exists());
        assertTrue(new File(dest, "gradle.properties").exists());
        assertTrue(new File(dest, "gradlew").exists());
        assertTrue(new File(dest, "settings.gradle").exists());
        assertTrue(new File(dest, "micronaut-cli.yml").exists());
        assertTrue(new File(dest, "README.md").exists());
        assertTrue(new File(dest, "LICENSEHEADER").exists());
        assertTrue(new File(dest, "src/main/resources/application.yml").exists());
        assertTrue(new File(dest, "src/main/resources/logback.xml").exists());
        assertTrue(new File(dest, "src/main/java/example/micronaut/CliCommand.java").exists());
        assertTrue(new File(dest, "src/test/java/example/micronaut/CliCommandTest.java").exists());

        // read the content of build.gradle
        File buildGradleFile = new File(dest, "build.gradle");
        String result = readFile(buildGradleFile);
        assertTrue(result.contains("""
                dependencies {
                    annotationProcessor("info.picocli:picocli-codegen")
                    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
                    implementation("info.picocli:picocli")
                    implementation("io.micronaut.mqtt:micronaut-mqttv5")
                    implementation("io.micronaut.picocli:micronaut-picocli")
                    implementation("io.micronaut.serde:micronaut-serde-jackson")
                    runtimeOnly("ch.qos.logback:logback-classic")
                    runtimeOnly("org.yaml:snakeyaml")
                }"""));
        assertTrue(result.contains("""
                micronaut {
                    testRuntime("junit5")
                    processing {
                        incremental(true)
                        annotations("example.micronaut.*")
                    }
                    testResources {
                        sharedServer = true
                    }
                }"""));
        String javaVersion = JavaVersion.current().getMajorVersion();

        assertTrue(result.contains("""
                application {
                    mainClass = "example.micronaut.CliCommand"
                }
                java {
                    sourceCompatibility = JavaVersion.toVersion("17")
                    targetCompatibility = JavaVersion.toVersion("17")
                }""".replace("17", javaVersion)));

    }

    @Test
    void testGenerateMultipleApps() throws Exception {
        File outputDirectory = new File("build/tmp/test");
        outputDirectory.mkdir();

        String path = "src/test/resources/guides";
        File file = new File(path);

        List<Guide> metadatas = GuideUtils.parseGuidesMetadata(file, "metadata.json", jsonSchemaProvider.getSchema(), jsonMapper);
        Guide guide = metadatas.get(4);

        assertDoesNotThrow(() -> guideProjectGenerator.generate(outputDirectory, guide));

        for (App app : guide.apps()) {
            File dest = Paths.get(outputDirectory.getAbsolutePath(), MacroUtils.getSourceDir(guide.slug(), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT)), app.name()).toFile();
            assertTrue(new File(dest, "build.gradle").exists());
            assertTrue(new File(dest, "gradlew.bat").exists());
            assertTrue(new File(dest, "gradlew").exists());
            assertTrue(new File(dest, "settings.gradle").exists());
            assertTrue(new File(dest, "LICENSEHEADER").exists());
            assertTrue(new File(dest, "src/main/java/example/micronaut/Application.java").exists());

            File buildGradleFile = new File(dest, "build.gradle");
            String result = readFile(buildGradleFile);

            for (String feature : app.features()) {
                assertTrue(result.contains(feature));
            }
        }
    }

    private String readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
