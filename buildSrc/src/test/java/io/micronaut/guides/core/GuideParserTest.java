package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
public class GuideParserTest {

    @Inject
    GuideParser guideParser;

    @Test
    void schemaAdvertisesSupportedPythonOptionsOnly(ResourceLoader resourceLoader) throws IOException, JSONException {
        try (var input = resourceLoader.getResourceAsStream("classpath:guide-metadata.schema.json").orElseThrow()) {
            JSONObject properties = new JSONObject(new String(input.readAllBytes(), StandardCharsets.UTF_8))
                    .getJSONObject("properties");
            assertFalse(properties.has("python"));
            assertTrue(properties.getJSONObject("languages").getJSONObject("items")
                    .getJSONArray("enum").toString().contains("\"PYTHON\""));
            assertTrue(properties.getJSONObject("apps").getJSONObject("items")
                    .getJSONObject("properties").has("pythonFeatures"));
        }
    }

    @Test
    void explicitPythonLanguageAddsPyronautBuildTool() {
        Guide guide = guideParser.parseGuideMetadata(
                new File("src/test/resources/guides-python/creating-your-first-micronaut-app"),
                "metadata.json").orElseThrow();
        assertEquals(List.of(Language.PYTHON), guide.languages());
        assertEquals(List.of(BuildTool.GRADLE, BuildTool.MAVEN, BuildTool.PYRONAUT), guide.buildTools());
    }

    @Test
    void childLanguageDefaultsAreAppliedBeforeBaseAppsAreMerged(@TempDir Path guides) throws Exception {
        Path base = Files.createDirectory(guides.resolve("base"));
        Files.writeString(base.resolve("metadata.json"), """
                {"publish":false,"languages":["PYTHON"],"apps":[{"name":"default"}]}
                """);
        Path child = Files.createDirectory(guides.resolve("child"));
        JSONObject metadata = new JSONObject(Files.readString(
                Path.of("src/test/resources/guides-python/creating-your-first-micronaut-app/metadata.json")));
        metadata.remove("languages");
        metadata.put("base", "base");
        metadata.put("apps", new JSONArray());
        for (boolean python : List.of(false, true)) {
            if (python) {
                metadata.put("languages", new JSONArray(List.of("PYTHON")));
            }
            Files.writeString(child.resolve("metadata.json"), metadata.toString());
            Guide parsed = guideParser.parseGuidesMetadata(guides.toFile(), "metadata.json").stream()
                    .filter(guide -> guide.slug().equals("child")).findFirst().orElseThrow();
            assertEquals(python ? List.of(Language.PYTHON) : List.of(Language.JAVA, Language.GROOVY, Language.KOTLIN), parsed.languages());
            assertEquals(python, parsed.buildTools().contains(BuildTool.PYRONAUT));
            assertEquals(1, parsed.apps().size());
            assertEquals("default", parsed.apps().get(0).name());
        }
    }

    @Test
    void testParseGuidesMetadata() {
        String path = "src/test/resources/guides";
        File file = new File(path);

        List<Guide> metadatas = guideParser.parseGuidesMetadata(file,"metadata.json");

        assertEquals(5,metadatas.size());

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
        assertFalse(guide.languages().contains(Language.PYTHON));
        assertTrue(guide.zipIncludes().isEmpty());
        assertTrue(guide.env().isEmpty());
        assertFalse(guideParser.parseGuideMetadata(new File(path, "child"), "metadata.json").orElseThrow().languages().contains(Language.PYTHON));
        List<String> tags = guide.tags();
        Collections.sort(tags);
        assertEquals(List.of("Azure", "cloud", "data-jdbc", "database", "flyway", "jdbc", "micronaut-data", "mysql"), tags);
        List<App> apps = guide.apps();
        assertNotNull(apps);
        assertEquals(1, apps.size());
        assertTrue(apps.stream().anyMatch(app -> app.name().equals("default") &&
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
                app.validateLicense()));

        guide = metadatas.get(4);
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
        assertTrue(apps.stream().anyMatch(app -> app.name().equals("springboot") &&
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
                app.validateLicense()));
        assertTrue(apps.stream().anyMatch(app -> app.name().equals("micronautframeworkjacksondatabind") &&
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
                app.validateLicense()));
        assertTrue(apps.stream().anyMatch(app -> app.name().equals("micronautframeworkserde") &&
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
                app.validateLicense()));
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
