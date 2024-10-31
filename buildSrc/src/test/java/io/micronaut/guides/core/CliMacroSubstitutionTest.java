package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class CliMacroSubstitutionTest {

    @Inject
    CliMacroSubstitution cliMacroSubstitution;

    private GuidesOption option;

    @BeforeEach
    void setup() {
        option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
    }

    @Test
    void testSubstitute(){
        App app = new App(
                "default",
                "example.micronaut",
                ApplicationType.DEFAULT,
                "Micronaut",
                List.of(),
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
                [source,bash]
                ----
                mn @cli-command@ example.micronaut.micronautguide --build=@build@ --lang=@lang@
                ----""";
        String result = cliMacroSubstitution.substitute(str, guide, option);
        String expected = """
                [source,bash]
                ----
                mn create-app example.micronaut.micronautguide --build=@build@ --lang=@lang@
                ----""";
        assertEquals(expected, result);
    }

    @Test
    void testSubstituteWithTarget(){
        App app = new App(
                "cli",
                "example.micronaut",
                ApplicationType.CLI,
                "Micronaut",
                List.of(),
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
                [source,bash]
                ----
                mn @cli:cli-command@ example.micronaut.micronautguide --build=@build@ --lang=@lang@
                ----""";
        String result = cliMacroSubstitution.substitute(str, guide, option);
        String expected = """
                [source,bash]
                ----
                mn create-cli-app example.micronaut.micronautguide --build=@build@ --lang=@lang@
                ----""";
        assertEquals(expected, result);
    }
}
