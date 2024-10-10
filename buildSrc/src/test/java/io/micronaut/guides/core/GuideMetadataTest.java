package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.json.JsonMapper;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class GuideMetadataTest {

    private App app;
    private Guide guide;

    private String expected = """
                {
                  "title": "Micronaut Guide",
                  "intro": "An introduction to building applications with Micronaut",
                  "authors": [
                    "John Doe",
                    "Jane Smith"
                  ],
                  "categories": [
                    "Micronaut",
                    "Java",
                    "Framework"
                  ],
                  "publicationDate": "2024-10-10",
                  "minimumJavaVersion": 11,
                  "maximumJavaVersion": 17,
                  "cloud": "OCI",
                  "skipGradleTests": false,
                  "skipMavenTests": false,
                  "asciidoctor": "micronaut-guide.adoc",
                  "languages": [
                    "JAVA",
                    "KOTLIN"
                  ],
                  "tags": [
                    "tutorial",
                    "example"
                  ],
                  "buildTools": [
                    "MAVEN",
                    "GRADLE"
                  ],
                  "testFramework": "JUNIT",
                  "zipIncludes": [
                    "src/docs/extra-file.txt"
                  ],
                  "slug": "micronaut-guide",
                  "publish": true,
                  "apps": {
                    "name": "default",
                    "packageName": "com.example.micronaut",
                    "applicationType": "DEFAULT",
                    "framework": "Micronaut",
                    "features": [
                      "http-client",
                      "data-jdbc"
                    ],
                    "invisibleFeatures": [
                      "invisible-feature-1"
                    ],
                    "kotlinFeatures": [
                      "kotlin-coroutines"
                    ],
                    "javaFeatures": [
                      "java-logging"
                    ],
                    "testFramework": "JUnit5",
                    "excludeTest": [
                      "ExcludedTest1",
                      "ExcludedTest2"
                    ],
                    "validateLicense": true
                  }
                }
            """;

    @BeforeEach
    void setup() {
        app = new App(
                "default", // app name
                "com.example.micronaut", // package name
                ApplicationType.DEFAULT, // application type
                "Micronaut", // framework
                List.of("http-client", "data-jdbc"), // required features
                List.of("invisible-feature-1"), // invisible features
                List.of("kotlin-coroutines"), // Kotlin features
                List.of("java-logging"), // Java features
                "JUnit5", // test framework
                List.of("ExcludedTest1", "ExcludedTest2"), // tests to exclude
                true // validate license
        );

        guide = new Guide(
                "Micronaut Guide",
                "An introduction to building applications with Micronaut",
                List.of("John Doe", "Jane Smith"),
                List.of("Micronaut", "Java", "Framework"),
                LocalDate.of(2024, 10, 10),
                11, // minimum Java version
                17, // maximum Java version
                Cloud.OCI, // Cloud provider
                false, // skipGradleTests
                false, // skipMavenTests
                "micronaut-guide.adoc", // asciidoctor file
                List.of(Guide.Language.JAVA, Guide.Language.KOTLIN),
                List.of("tutorial", "example"), // tags
                List.of(Guide.BuildTool.MAVEN, Guide.BuildTool.GRADLE), // build tools
                Guide.TestFramework.JUNIT, // test framework
                List.of("src/docs/extra-file.txt"), // additional files for the zip
                "micronaut-guide", // slug
                true, // publish
                null, // base guide
                null, // environment variables
                app // Placeholder for the App object
        );
    }

    @Test
    void serializeGuideToJson(JsonMapper json, ResourceLoader resourceLoader) throws IOException {
        String result = json.writeValueAsString(guide);
        assertEquals(expected.replaceAll("\\s+", ""), result.replaceAll("\\s+", ""));
    }

    @Test
    void deserializeGuideFromJson(JsonMapper json) throws IOException {
        assertEquals(guide, json.readValue(expected, Guide.class));
    }
}
