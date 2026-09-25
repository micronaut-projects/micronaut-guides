package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.json.JsonMapper;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
        assertFalse(GuideUtils.shouldSkip(guide, BuildTool.GRADLE, Language.JAVA));
        assertFalse(GuideUtils.shouldSkip(guide, BuildTool.MAVEN, Language.JAVA));
        assertTrue(GuideUtils.shouldSkip(guide, BuildTool.MAVEN, Language.KOTLIN));
        assertFalse(GuideUtils.isSupported(BuildTool.MAVEN, Language.KOTLIN));
    }

    @Test
    void testShouldSkipTrueKotlin() {
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-skip.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        assertTrue(GuideUtils.shouldSkip(guide, BuildTool.GRADLE_KOTLIN, Language.JAVA));
        assertFalse(GuideUtils.shouldSkip(guide, BuildTool.MAVEN, Language.JAVA));
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

    @Test
    void testMergePreservesPythonOptionsFromBase() {
        App baseApp = new App("default", null, null, null, List.of(), List.of(), List.of(), List.of(), List.of(), null, null, null, true, List.of("base"));
        Guide base = new Guide("base", "base", List.of("author"), List.of("category"), LocalDate.of(2026, 1, 1), null, null, null,
                false, false, null, List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN, Language.PYTHON), List.of(),
                List.of(BuildTool.GRADLE, BuildTool.MAVEN, BuildTool.PYRONAUT), null, List.of(), "base", true, null, Map.of(), List.of(baseApp), true, false);
        App childApp = new App("default", null, null, null, List.of(), List.of(), List.of(), List.of(), List.of(), null, null, null, true, List.of("child"));
        Guide child = new Guide("child", "child", List.of(), List.of("category"), LocalDate.of(2026, 1, 1), null, null, null,
                false, false, null, List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN), List.of(),
                List.of(BuildTool.GRADLE, BuildTool.MAVEN), null, List.of(), "child", true, "base", Map.of(), List.of(childApp), null, false);

        Guide merged = GuideUtils.merge(base, child);

        assertTrue(merged.python());
        assertTrue(merged.languages().contains(Language.PYTHON));
        assertTrue(merged.buildTools().contains(BuildTool.PYRONAUT));
        assertEquals(List.of("child", "base"), merged.apps().get(0).pythonFeatures());
    }

    @Test
    void testMergeAllowsChildToDisablePython() {
        App app = new App("default", null, null, null, List.of(), List.of(), List.of(), List.of(), List.of(), null, null, null, true, List.of());
        Guide base = new Guide("base", "base", List.of("author"), List.of("category"), LocalDate.of(2026, 1, 1), null, null, null,
                false, false, null, List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN, Language.PYTHON), List.of(),
                List.of(BuildTool.GRADLE, BuildTool.MAVEN, BuildTool.PYRONAUT), null, List.of(), "base", true, null, Map.of(), List.of(app), true, false);
        Guide child = new Guide("child", "child", List.of(), List.of("category"), LocalDate.of(2026, 1, 1), null, null, null,
                false, false, null, List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN), List.of(),
                List.of(BuildTool.GRADLE, BuildTool.MAVEN), null, List.of(), "child", true, "base", Map.of(), List.of(app), false, false);

        Guide merged = GuideUtils.merge(base, child);

        assertFalse(merged.python());
        assertFalse(merged.languages().contains(Language.PYTHON));
        assertFalse(merged.buildTools().contains(BuildTool.PYRONAUT));
    }
}
