package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class RawTestMacroSubstitutionTest {
    @Inject
    private RawTestMacroSubstitution rawTestMacroSubstitution;

    @Test
    void testSubstitute() {
        String str = "rawTest:HomePage[]";
        String resJava = rawTestMacroSubstitution.substitute(str, "micronaut-security-jwt-cookie", new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.SPOCK));
        String expectedJava = """
                [source,groovy]
                .src/test/groovy/example/micronaut/HomePage.groovy
                ----
                include::{sourceDir}/micronaut-security-jwt-cookie/micronaut-security-jwt-cookie-gradle-java/src/test/groovy/example/micronaut/HomePage.groovy[]
                ----
                """;
        assertEquals(expectedJava, resJava);
        String resKotlin = rawTestMacroSubstitution.substitute(str, "micronaut-security-jwt-cookie", new GuidesOption(BuildTool.GRADLE, Language.KOTLIN, TestFramework.SPOCK));
        String expectedKotlin = """
                [source,groovy]
                .src/test/groovy/example/micronaut/HomePage.groovy
                ----
                include::{sourceDir}/micronaut-security-jwt-cookie/micronaut-security-jwt-cookie-gradle-kotlin/src/test/groovy/example/micronaut/HomePage.groovy[]
                ----
                """;
        assertEquals(expectedKotlin, resKotlin);
        String resGroovy = rawTestMacroSubstitution.substitute(str, "micronaut-security-jwt-cookie", new GuidesOption(BuildTool.GRADLE, Language.GROOVY, TestFramework.SPOCK));
        String expectedGroovy = """
                [source,groovy]
                .src/test/groovy/example/micronaut/HomePage.groovy
                ----
                include::{sourceDir}/micronaut-security-jwt-cookie/micronaut-security-jwt-cookie-gradle-groovy/src/test/groovy/example/micronaut/HomePage.groovy[]
                ----
                """;
        assertEquals(expectedGroovy, resGroovy);
    }
}
