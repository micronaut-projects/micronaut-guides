package io.micronaut.guides.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.context.BeanContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.type.Argument;
import io.micronaut.json.JsonMapper;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.NotBlank;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GuideTest {

    @Inject
    Validator validator;

    @Inject
    BeanContext beanContext;

    @Inject
    JsonMapper jsonMapper;

    @Inject
    ResourceLoader resourceLoader;

    @Test
    void testGuideWithBase() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(Guide.class));
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:base.json");
        assertTrue(inputStreamOptional.isPresent());
        final InputStream inputStreamBase = inputStreamOptional.get();
        Guide base = assertDoesNotThrow(() -> jsonMapper.readValue(inputStreamBase, Guide.class));

        inputStreamOptional = resourceLoader.getResourceAsStream("classpath:child.json");
        assertTrue(inputStreamOptional.isPresent());
        final InputStream inputStreamChild = inputStreamOptional.get();
        Guide child = assertDoesNotThrow(() -> jsonMapper.readValue(inputStreamChild, Guide.class));
        Guide guide = GuideUtils.merge(base, child);
        assertEquals(List.of("Graeme Rocher"), guide.authors());
        assertEquals("Connect a Micronaut Data JDBC Application to Azure Database for MySQL", guide.title());
        assertEquals("Learn how to connect a Micronaut Data JDBC application to a Microsoft Azure Database for MySQL", guide.intro());
        assertEquals(List.of("Data JDBC"), guide.categories());
        assertEquals(LocalDate.of(2022,2, 17), guide.publicationDate());
        List<String> tags = guide.tags();
        Collections.sort(tags);
        assertEquals(List.of("Azure","cloud", "database", "flyway", "jdbc", "micronaut-data", "mysql"), tags);
        List<App> apps = guide.apps();
        assertNotNull(apps);
        assertEquals(1, apps.size());
        assertTrue(apps.stream().anyMatch(app -> {
            return app.name().equals("default") &&
                    app.applicationType() == ApplicationType.DEFAULT &&
                    app.packageName().equals("example.micronaut") &&
                    app.framework().equals("Micronaut") &&
                    app.features() == null &&
                    app.invisibleFeatures() ==  null &&
                    app.kotlinFeatures() ==  null &&
                    app.javaFeatures() ==  null &&
                    app.groovyFeatures() ==  null &&
                    app.testFramework() ==  null &&
                    app.excludeTest() ==  null &&
                    app.validateLicense();
        }));
    }

    @Test
    void typeCloudCanBeNull() {
        String title = "1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API";
        String intro = "This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.";
        List<String> categories = List.of("Boot to Micronaut Building a REST API");
        List<String> authors = List.of("Sergio del Amo");
        LocalDate publicationDate = LocalDate.of(2024, 4, 24);
        List<App> apps = new ArrayList<>();
        apps.add(new App("springboot", null, null, null, null, null, null, null, null, null, null, false));
        Set<ConstraintViolation<Guide>> violations = validator.validate(
                new Guide(title,intro, authors, categories, publicationDate, null, null, null,false,false,null,null,null,null,null,null,null,true,null,null,apps));
        assertTrue(violations.isEmpty());
    }

    @Test
    void testDeserialization() throws IOException {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        assertEquals(List.of("Sergio del Amo"), guide.authors());
        assertEquals("1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API", guide.title());
        assertEquals("This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.", guide.intro());
        assertEquals(List.of("spring-boot"), guide.tags());
        assertEquals(List.of("Boot to Micronaut Building a REST API"), guide.categories());
        assertEquals(LocalDate.of(2024, 4, 24), guide.publicationDate());
        assertEquals(List.of(Language.JAVA), guide.languages());
        assertEquals(List.of(BuildTool.GRADLE), guide.buildTools());
        List<App> apps = guide.apps();
        assertNotNull(apps);
        assertEquals(3, apps.size());
        assertTrue(apps.stream().anyMatch(app -> {
                    return app.name().equals("springboot") &&
                            app.applicationType() == ApplicationType.DEFAULT &&
                            app.packageName().equals("example.micronaut") &&
                            app.framework().equals("Spring Boot") &&
                            app.features().equals(List.of("spring-boot-starter-web")) &&
                            app.invisibleFeatures() ==  null &&
                            app.kotlinFeatures() ==  null &&
                            app.javaFeatures() ==  null &&
                            app.groovyFeatures() ==  null &&
                            app.testFramework() ==  null &&
                            app.excludeTest() ==  null &&
                            app.validateLicense();
        }));
        assertTrue(apps.stream().anyMatch(app -> {
            return app.name().equals("micronautframeworkjacksondatabind") &&
                    app.applicationType() == ApplicationType.DEFAULT &&
                    app.packageName().equals("example.micronaut") &&
                    app.framework().equals("Micronaut") &&
                    app.features().equals(List.of("json-path", "assertj", "jackson-databind")) &&
                    app.invisibleFeatures() ==  null &&
                    app.kotlinFeatures() ==  null &&
                    app.javaFeatures() ==  null &&
                    app.groovyFeatures() ==  null &&
                    app.testFramework() ==  null &&
                    app.excludeTest() ==  null &&
                    app.validateLicense();
        }));
        assertTrue(apps.stream().anyMatch(app -> {
            return app.name().equals("micronautframeworkserde") &&
                    app.applicationType() == ApplicationType.DEFAULT &&
                    app.packageName().equals("example.micronaut") &&
                    app.framework().equals("Micronaut") &&
                    app.features().equals(List.of("json-path", "assertj")) &&
                    app.invisibleFeatures() ==  null &&
                    app.kotlinFeatures() ==  null &&
                    app.groovyFeatures() ==  null &&
                    app.javaFeatures() ==  null &&
                    app.testFramework() ==  null &&
                    app.excludeTest() ==  null &&
                    app.validateLicense();
        }));
        assertFalse(guide.skipGradleTests());
        assertFalse(guide.skipMavenTests());
        assertNull(guide.minimumJavaVersion());
        assertNull(guide.maximumJavaVersion());
        assertNull(guide.cloud());
        assertNull(guide.asciidoctor());
        assertTrue(guide.publish());
        assertNull(guide.asciidoctor());
        assertNull(guide.slug());
        assertNull(guide.zipIncludes());
        assertNull(guide.base());
        assertNull(guide.env());
    }

    @Test
    void testGetTags(){
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        List<String> expectedList = List.of("assertj", "boot-to-micronaut-building-a-rest-api", "jackson-databind", "json-path", "spring-boot", "spring-boot-starter-web");
        List<String> actualList = GuideUtils.getTags(guide);
        Collections.sort(actualList);
        assertEquals(expectedList, actualList);
    }

    @Test
    void testGetAppFeaturesWithoutValidateLicense(){
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-features.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        App app =  assertDoesNotThrow(() -> guide.apps().stream().filter(el -> el.name().equals("secondApp")).findFirst().get());

        assertEquals(List.of("invisible"),GuideUtils.getAppInvisibleFeatures(app));

        List javaAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.JAVA);
        List kotlinAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.KOTLIN);
        List groovyAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.GROOVY);
        Collections.sort(javaAppVisibleFeatures);
        Collections.sort(kotlinAppVisibleFeatures);
        Collections.sort(groovyAppVisibleFeatures);
        assertEquals(List.of("awaitility", "graalvm", "mqtt", "yaml"),javaAppVisibleFeatures);
        assertEquals(List.of("graalvm", "kapt", "mqtt", "yaml"),kotlinAppVisibleFeatures);
        assertEquals(List.of("graalvm", "groovy-toml", "mqtt", "yaml"),groovyAppVisibleFeatures);

        List javaAppFeatures = GuideUtils.getAppFeatures(app,Language.JAVA);
        List kotlinAppFeatures = GuideUtils.getAppFeatures(app,Language.KOTLIN);
        List groovyAppFeatures = GuideUtils.getAppFeatures(app,Language.GROOVY);
        Collections.sort(javaAppFeatures);
        Collections.sort(kotlinAppFeatures);
        Collections.sort(groovyAppFeatures);
        assertEquals(List.of("awaitility", "graalvm", "invisible", "mqtt", "yaml"),javaAppFeatures);
        assertEquals(List.of("graalvm", "invisible", "kapt", "mqtt", "yaml"),kotlinAppFeatures);
        assertEquals(List.of("graalvm", "groovy-toml", "invisible", "mqtt", "yaml"),groovyAppFeatures);
    }

    @Test
    void testGetAppFeatures(){
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-features.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        App app =  assertDoesNotThrow(() -> guide.apps().stream().filter(el -> el.name().equals("app")).findFirst().get());

        assertEquals(List.of("invisible", "spotless"),GuideUtils.getAppInvisibleFeatures(app));

        List javaAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.JAVA);
        List kotlinAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.KOTLIN);
        List groovyAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.GROOVY);
        Collections.sort(javaAppVisibleFeatures);
        Collections.sort(kotlinAppVisibleFeatures);
        Collections.sort(groovyAppVisibleFeatures);
        assertEquals(List.of("awaitility", "graalvm", "mqtt", "yaml"),javaAppVisibleFeatures);
        assertEquals(List.of("graalvm", "kapt", "mqtt", "yaml"),kotlinAppVisibleFeatures);
        assertEquals(List.of("graalvm", "groovy-toml", "mqtt", "yaml"),groovyAppVisibleFeatures);

        List javaAppFeatures = GuideUtils.getAppFeatures(app,Language.JAVA);
        List kotlinAppFeatures = GuideUtils.getAppFeatures(app,Language.KOTLIN);
        List groovyAppFeatures = GuideUtils.getAppFeatures(app,Language.GROOVY);
        Collections.sort(javaAppFeatures);
        Collections.sort(kotlinAppFeatures);
        Collections.sort(groovyAppFeatures);
        assertEquals(List.of("awaitility", "graalvm", "invisible", "mqtt", "spotless", "yaml"),javaAppFeatures);
        assertEquals(List.of("graalvm", "invisible", "kapt", "mqtt", "spotless", "yaml"),kotlinAppFeatures);
        assertEquals(List.of("graalvm", "groovy-toml", "invisible", "mqtt", "spotless", "yaml"),groovyAppFeatures);
    }

    @Test
    void testGetAppFeaturesEmpty(){
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-features.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        App app =  assertDoesNotThrow(() -> guide.apps().stream().filter(el -> el.name().equals("thirdApp")).findFirst().get());

        assertEquals(List.of("spotless"),GuideUtils.getAppInvisibleFeatures(app));

        List javaAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.JAVA);
        List kotlinAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.KOTLIN);
        List groovyAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app,Language.GROOVY);
        Collections.sort(javaAppVisibleFeatures);
        Collections.sort(kotlinAppVisibleFeatures);
        Collections.sort(groovyAppVisibleFeatures);
        assertEquals(List.of(),javaAppVisibleFeatures);
        assertEquals(List.of(),kotlinAppVisibleFeatures);
        assertEquals(List.of(),groovyAppVisibleFeatures);

        List javaAppFeatures = GuideUtils.getAppFeatures(app,Language.JAVA);
        List kotlinAppFeatures = GuideUtils.getAppFeatures(app,Language.KOTLIN);
        List groovyAppFeatures = GuideUtils.getAppFeatures(app,Language.GROOVY);
        Collections.sort(javaAppFeatures);
        Collections.sort(kotlinAppFeatures);
        Collections.sort(groovyAppFeatures);
        assertEquals(List.of("spotless"),javaAppFeatures);
        assertEquals(List.of("spotless"),kotlinAppFeatures);
        assertEquals(List.of("spotless"),groovyAppFeatures);
    }

    @Test
    void testShouldSkip() throws IOException {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        assertEquals(GuideUtils.shouldSkip(guide,BuildTool.GRADLE), false);
        assertEquals(GuideUtils.shouldSkip(guide,BuildTool.MAVEN), false);
    }

    @Test
    void testShouldSkipTrueKotlin() throws IOException {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-skip.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        assertEquals(GuideUtils.shouldSkip(guide,BuildTool.GRADLE_KOTLIN), true);
        assertEquals(GuideUtils.shouldSkip(guide,BuildTool.MAVEN), false);
    }

    @Test
    void typeAppIsAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(Guide.class));
    }

    @Test
    void typeGuideIsDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Guide.class)));
    }

    @Test
    void typeGuideIsNotSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Guide.class)));
    }

    @Test
    void buildGeneratesJsonSchema(ResourceLoader resourceLoader) {
        assertTrue(resourceLoader.getResource("META-INF/schemas/guide.schema.json").isPresent());
    }

    @Test
    void defaultValuesAreSetCorrectly() {
        Guide guide = new Guide(null,null, null, null, null, null, null, null,false,false,null,null,null,null,null,null,null,true,null,null,null);

        assertEquals(guide.publish(),true);
    }

    @Test
    void buildGeneratesCorrectJsonSchema(ResourceLoader resourceLoader) throws IOException, JSONException {
        Optional<URL> res = resourceLoader.getResource("META-INF/schemas/guide.schema.json");

        InputStream is = res.get().openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

        String schema = reader.lines().collect(Collectors.joining("\n"));
        String expected = """
            {
              "$schema": "https://json-schema.org/draft/2020-12/schema",
              "$id": "https://guides.micronaut.io/schemas/guide.schema.json",
              "title": "Guide",
              "type": [
                "object"
              ],
              "properties": {
                "apps": {
                  "description": "Applications created for the guide",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "$ref": "https://guides.micronaut.io/schemas/app.schema.json"
                  }
                },
                "asciidoctor": {
                  "description": "The guide asciidoc file. If not specified, the guide slug followed by the .adoc suffix is used",
                  "type": [
                    "string"
                  ]
                },
                "authors": {
                  "description": "The guide's authors",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "base": {
                  "description": "Defaults to null; if set, indicates directory name of the base guide to copy before copying the current one",
                  "type": [
                    "string"
                  ]
                },
                "buildTools": {
                  "description": "By default the code in the guide is generated for Gradle and Maven. If a guide is specific only for a build tool, define it here",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ],
                    "enum": [
                      "GRADLE",
                      "GRADLE_KOTLIN",
                      "MAVEN"
                    ]
                  }
                },
                "categories": {
                  "description": "The guide's categories",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "cloud": {
                  "description": "The acronym for the cloud service provider of the guide. For example, OCI for Oracle Cloud Infrastructure",
                  "type": [
                    "string"
                  ],
                  "enum": [
                    "OCI",
                    "AWS",
                    "AZURE",
                    "GCP"
                  ]
                },
                "env": {
                  "description": "The guide's environment variables",
                  "type": [
                    "object"
                  ],
                  "additionalProperties": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "intro": {
                  "description": "The guide introduction",
                  "type": [
                    "string"
                  ]
                },
                "languages": {
                  "description": "The guide supported languages",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ],
                    "enum": [
                      "JAVA",
                      "GROOVY",
                      "KOTLIN"
                    ]
                  }
                },
                "maximumJavaVersion": {
                  "description": "If the guide needs a maximum Java version, define it here",
                  "type": [
                    "integer"
                  ]
                },
                "minimumJavaVersion": {
                  "description": "If the guide needs a minimum Java version, define it here",
                  "type": [
                    "integer"
                  ]
                },
                "publicationDate": {
                  "description": "The guide publication date. It should follow the format YYYY-MM-DD",
                  "type": [
                    "string"
                  ],
                  "format": "date"
                },
                "publish": {
                  "description": "Whether the guide should be published, it defaults to true. You can set it to false for draft or base guides",
                  "type": [
                    "boolean"
                  ]
                },
                "skipGradleTests": {
                  "description": "Set it to true to skip running the tests for the Gradle applications for the guide",
                  "type": [
                    "boolean"
                  ]
                },
                "skipMavenTests": {
                  "description": "Set it to true to skip running the tests for the Maven applications for the guide",
                  "type": [
                    "boolean"
                  ]
                },
                "slug": {
                  "description": "The guide's slug. If not specified, the guides folder is used",
                  "type": [
                    "string"
                  ]
                },
                "tags": {
                  "description": "List of tags added to the guide. features are added automatically as tags. No need to repeat them here",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                },
                "testFramework": {
                  "description": "The guide's test framework. By default Java and Kotlin applications are tested with JUnit5 and Groovy applications with Spock",
                  "type": [
                    "string"
                  ],
                  "enum": [
                    "JUNIT",
                    "SPOCK",
                    "KOTEST"
                  ]
                },
                "title": {
                  "description": "The guide's title",
                  "type": [
                    "string"
                  ]
                },
                "zipIncludes": {
                  "description": "List of additional files with a relative path to include in the generated zip file for the guide",
                  "type": [
                    "array"
                  ],
                  "items": {
                    "type": [
                      "string"
                    ]
                  }
                }
              },
              "required": [
                "title",
                "intro",
                "authors",
                "categories",
                "publicationDate",
                "apps"
              ]
            }
                """;
        System.out.println(schema);
        JSONAssert.assertEquals(expected, schema, JSONCompareMode.LENIENT);
    }


}
