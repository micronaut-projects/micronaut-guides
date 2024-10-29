package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class TestResourceMacroSubstitutionTest {

    @Inject
    TestResourceMacroSubstitution testResourceMacroSubstitution;

    @Test
    void testSubstitute() {
        String str = "testResource:application-test.yml[tag=testcontainers]";
        String resJava = testResourceMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-metrics-oci"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                [source,yaml]
                .src/test/resources/application-test.yml
                ----
                include::{sourceDir}/micronaut-metrics-oci/micronaut-metrics-oci-gradle-java/src/test/resources/application-test.yml[tag=testcontainers]
                ----""";
        assertEquals(expectedJava, resJava);
    }
}
