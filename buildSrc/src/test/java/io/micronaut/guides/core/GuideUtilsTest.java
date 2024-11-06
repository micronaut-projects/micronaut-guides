package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.json.JsonMapper;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GuideUtilsTest {
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
        assertFalse(GuideUtils.shouldSkip(guide, BuildTool.GRADLE));
        assertFalse(GuideUtils.shouldSkip(guide, BuildTool.MAVEN));
    }

    @Test
    void testShouldSkipTrueKotlin() {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-skip.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        assertTrue(GuideUtils.shouldSkip(guide, BuildTool.GRADLE_KOTLIN));
        assertFalse(GuideUtils.shouldSkip(guide, BuildTool.MAVEN));
    }

    @Test
    void testGetFrameworks() {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        Set<String> frameworks = GuideUtils.getFrameworks(guide);
        Set<String> expected = Set.of("Spring Boot","Micronaut");
        assertEquals(expected,frameworks);
    }
}
