package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class FeaturesMacroSubstitutionTest {

    @Inject
    FeaturesMacroSubstitution featuresMacroSubstitution;

    @Test
    void testSubstitute(){
        App app = new App(
                "cli",
                "example.micronaut",
                ApplicationType.CLI,
                "Micronaut",
                List.of("yaml","mqtt"),
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
                LocalDate.of(2024,4,24),
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
        String str = """
                common:cli-or-launch.adoc[]
                
                [source,bash]
                ----
                mn @cli:cli-command@ example.micronaut.micronautguide \\
                    --features=@cli:features@ \\
                    --build=@build@ --lang=@lang@
                ----
                
                common:build-lang-arguments.adoc[]""";
        String result = featuresMacroSubstitution.substitute(str, guide, new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expected = """
                common:cli-or-launch.adoc[]
                
                [source,bash]
                ----
                mn @cli:cli-command@ example.micronaut.micronautguide \\
                    --features=yaml,mqtt \\
                    --build=@build@ --lang=@lang@
                ----
                
                common:build-lang-arguments.adoc[]""";
        assertEquals(expected, result);
    }
}
