package io.micronaut.guides.core;

import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.json.JsonMapper;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class BuildDiffLinkSubstitutionTest {
    @Inject
    BuildDiffLinkSubstitution buildDiffLinkSubstitution;

    @Inject
    JsonMapper jsonMapper;

    @Inject
    ResourceLoader resourceLoader;

    @Test
    void testSubstitute(){
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(Guide.class));
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-diff.json");
        assertTrue(inputStreamOptional.isPresent());
        final InputStream inputStreamBase = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStreamBase, Guide.class));
        String str = "diffLink:[app=cli]";
        String resJava = buildDiffLinkSubstitution.substitute(str, guide, new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        URI expectedURI = UriBuilder.of("https://micronaut.io")
                .path("launch")
                .queryParam("lang", "JAVA")
                .queryParam("build", "GRADLE")
                .queryParam("test", "JUNIT")
                .queryParam("name", "cli")
                .queryParam("type", "CLI")
                .queryParam("package", "example.micronaut")
                .queryParam("activity", "diff")
                .queryParam("features", "awaitility")
                .queryParam("features", "graalvm")
                .queryParam("features", "mqtt")
                .queryParam("features", "yaml")
                .build();
        String expectedJava = expectedURI.toString() + "[Diff]";
        assertEquals(expectedJava, resJava);

        str = "diffLink:[app=cli,featureExcludes=graalvm]";
        resJava = buildDiffLinkSubstitution.substitute(str, guide, new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        expectedURI = UriBuilder.of("https://micronaut.io")
                .path("launch")
                .queryParam("lang", "JAVA")
                .queryParam("build", "GRADLE")
                .queryParam("test", "JUNIT")
                .queryParam("name", "cli")
                .queryParam("type", "CLI")
                .queryParam("package", "example.micronaut")
                .queryParam("activity", "diff")
                .queryParam("features", "awaitility")
                .queryParam("features", "mqtt")
                .queryParam("features", "yaml")
                .build();
        expectedJava = expectedURI.toString() + "[Diff]";
        assertEquals(expectedJava, resJava);
    }

}
