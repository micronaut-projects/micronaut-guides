package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.json.JsonMapper;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Inject;
import org.gradle.api.file.Directory;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GuideUtilsTest {
    @Inject
    JsonMapper jsonMapper;

    @Inject
    ResourceLoader resourceLoader;

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

        List javaAppVisibleFeatures = GuideUtils.getAppVisibleFeatures(app, Language.JAVA);
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
    void testShouldSkip() {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        assertEquals(GuideUtils.shouldSkip(guide, BuildTool.GRADLE), false);
        assertEquals(GuideUtils.shouldSkip(guide,BuildTool.MAVEN), false);
    }

    @Test
    void testShouldSkipTrueKotlin() {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-skip.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        assertEquals(GuideUtils.shouldSkip(guide,BuildTool.GRADLE_KOTLIN), true);
        assertEquals(GuideUtils.shouldSkip(guide,BuildTool.MAVEN), false);
    }

    @Test
    void testGetFrameworks() {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        Set<String> frameworks = GuideUtils.getFrameworks(guide);
        Set<String> expected = Set.of("Spring Boot","micronautframeworkjacksondatabind","micronautframeworkserde");
        assertEquals(frameworks,expected);
    }

    @Test
    void testParseGuidesMetadata() throws Exception {
        GuideUtils guideUtils = new GuideUtils();
        String path = "src/test/resources/guides";
        File file = new File(path);

        List<Guide> metadatas = guideUtils.parseGuidesMetadata(file,"metadata.json");

        assertEquals(3,metadatas.size());

        Guide guide = metadatas.get(1);
        assertEquals(List.of("Graeme Rocher"), guide.authors());
        assertEquals("Connect a Micronaut Data JDBC Application to Azure Database for MySQL", guide.title());
        assertEquals("Learn how to connect a Micronaut Data JDBC application to a Microsoft Azure Database for MySQL", guide.intro());
        assertEquals(List.of("Data JDBC"), guide.categories());
        assertEquals(LocalDate.of(2022,2, 17), guide.publicationDate());
        assertEquals("base",guide.base());
        assertEquals("child",guide.slug());
        assertEquals(List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN),guide.languages());
        assertEquals(List.of(BuildTool.GRADLE, BuildTool.MAVEN),guide.buildTools());
        assertTrue(guide.zipIncludes().isEmpty());
        assertTrue(guide.env().isEmpty());
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
                    app.features().isEmpty() &&
                    app.invisibleFeatures().isEmpty() &&
                    app.kotlinFeatures().isEmpty() &&
                    app.javaFeatures().isEmpty() &&
                    app.groovyFeatures().isEmpty() &&
                    app.testFramework() ==  null &&
                    app.excludeTest() ==  null &&
                    app.excludeSource() ==  null &&
                    app.validateLicense();
        }));

        guide = metadatas.get(2);
        assertEquals(List.of("Sergio del Amo"), guide.authors());
        assertEquals("1. Testing Serialization - Spring Boot vs Micronaut Framework - Building a Rest API", guide.title());
        assertEquals("This guide compares how to test serialization and deserialization with Micronaut Framework and Spring Boot.", guide.intro());
        assertEquals(List.of("spring-boot"), guide.tags());
        assertEquals(List.of("Boot to Micronaut Building a REST API"), guide.categories());
        assertEquals(LocalDate.of(2024, 4, 24), guide.publicationDate());
        assertEquals(List.of(Language.JAVA), guide.languages());
        assertEquals(List.of(BuildTool.GRADLE), guide.buildTools());
        apps = guide.apps();
        assertNotNull(apps);
        assertEquals(3, apps.size());
        assertTrue(apps.stream().anyMatch(app -> {
            return app.name().equals("springboot") &&
                    app.applicationType() == ApplicationType.DEFAULT &&
                    app.packageName().equals("example.micronaut") &&
                    app.framework().equals("Spring Boot") &&
                    app.features().equals(List.of("spring-boot-starter-web")) &&
                    app.invisibleFeatures().isEmpty() &&
                    app.kotlinFeatures().isEmpty() &&
                    app.javaFeatures().isEmpty() &&
                    app.groovyFeatures().isEmpty() &&
                    app.testFramework() ==  null &&
                    app.excludeTest() ==  null &&
                    app.excludeSource() ==  null &&
                    app.validateLicense();
        }));
        assertTrue(apps.stream().anyMatch(app -> {
            return app.name().equals("micronautframeworkjacksondatabind") &&
                    app.applicationType() == ApplicationType.DEFAULT &&
                    app.packageName().equals("example.micronaut") &&
                    app.framework().equals("Micronaut") &&
                    app.features().equals(List.of("json-path", "assertj", "jackson-databind")) &&
                    app.invisibleFeatures().isEmpty() &&
                    app.kotlinFeatures().isEmpty() &&
                    app.javaFeatures().isEmpty() &&
                    app.groovyFeatures().isEmpty() &&
                    app.testFramework() ==  null &&
                    app.excludeTest() ==  null &&
                    app.excludeSource() ==  null &&
                    app.validateLicense();
        }));
        assertTrue(apps.stream().anyMatch(app -> {
            return app.name().equals("micronautframeworkserde") &&
                    app.applicationType() == ApplicationType.DEFAULT &&
                    app.packageName().equals("example.micronaut") &&
                    app.framework().equals("Micronaut") &&
                    app.features().equals(List.of("json-path", "assertj")) &&
                    app.invisibleFeatures().isEmpty() &&
                    app.kotlinFeatures().isEmpty() &&
                    app.javaFeatures().isEmpty() &&
                    app.groovyFeatures().isEmpty() &&
                    app.testFramework() ==  null &&
                    app.excludeTest() ==  null &&
                    app.excludeSource() ==  null &&
                    app.validateLicense();
        }));
        assertFalse(guide.skipGradleTests());
        assertFalse(guide.skipMavenTests());
        assertNull(guide.minimumJavaVersion());
        assertNull(guide.maximumJavaVersion());
        assertNull(guide.cloud());
        assertTrue(guide.publish());
        assertEquals("test.adoc",guide.asciidoctor());
        assertEquals("test",guide.slug());
        assertTrue(guide.zipIncludes().isEmpty());
        assertNull(guide.base());
        assertTrue(guide.env().isEmpty());
    }
}
