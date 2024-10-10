package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.json.JsonMapper;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class GuideMetadataTest {

    @Nested
    class GenericTests{
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
                      "apps": [{
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
                      }]
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
                    List.of(Language.JAVA, Language.KOTLIN),
                    List.of("tutorial", "example"), // tags
                    List.of(BuildTool.MAVEN, BuildTool.GRADLE), // build tools
                    TestFramework.JUNIT, // test framework
                    List.of("src/docs/extra-file.txt"), // additional files for the zip
                    "micronaut-guide", // slug
                    true, // publish
                    null, // base guide
                    null, // environment variables
                    List.of(app) // Placeholder for the App object
            );
        }

        @Test
        void serializeGuideToJson(JsonMapper json, ResourceLoader resourceLoader) throws IOException, JSONException {
            String result = json.writeValueAsString(guide);
            JSONAssert.assertEquals(expected, result, JSONCompareMode.LENIENT);
        }

        @Test
        void deserializeGuideFromJson(JsonMapper json) throws IOException {
            assertEquals(guide, json.readValue(expected, Guide.class));
        }
    }

    @Test
    void deserializeGuideWithCorrectDefault(JsonMapper json) throws IOException {

        String expected = """
            {
              "title": "Micronaut Guide",
              "intro": "An introduction to building applications with Micronaut",
              "authors": [
                "John Doe",
                "Jane Smith"
              ],
              "languages": [
                "JAVA"
              ],
              "publicationDate": "2024-10-10",
              "buildTools": [
                "MAVEN",
                "GRADLE"
              ],
              "asciidoctor": "micronaut-guide.adoc",
              "slug": "micronaut-guide",
              "publish": true,
              "apps": [
                  {
                    "name": "default",
                    "testFramework": "JUnit5",
                    "excludeTest": [
                      "ExcludedTest1",
                      "ExcludedTest2"
                    ]
                  }
              ]
            }
        """;

        App app = new App(
                "default",
                "example.micronaut",
                ApplicationType.DEFAULT,
                "Micronaut",
                null,
                null,
                null,
                null,
                "JUnit5",
                List.of("ExcludedTest1", "ExcludedTest2"),
                false
        );

        Guide guide = new Guide(
                "Micronaut Guide",
                "An introduction to building applications with Micronaut",
                List.of("John Doe","Jane Smith"),
                List.of(),
                LocalDate.of(2024, 10, 10),
                null,
                null,
                null,
                false,
                false,
                "micronaut-guide.adoc",
                List.of(Language.JAVA),
                null,
                List.of(BuildTool.MAVEN, BuildTool.GRADLE),
                null,
                null,
                "micronaut-guide",
                true,
                null,
                null,
                List.of(app)
        );

        Guide guideFromJson = json.readValue(expected, Guide.class);

        assertEquals(guide, guideFromJson);
    }
}
