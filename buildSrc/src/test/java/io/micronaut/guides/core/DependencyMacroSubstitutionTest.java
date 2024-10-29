package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
public class DependencyMacroSubstitutionTest {
    @Inject
    DependencyMacroSubstitution dependencyMacroSubstitution;

    @Test
    void testSubstitute(){
        String str = """
                ==== Dependencies
                
                When you add a `jax-rs` feature, the generated application includes the following dependencies:
                
                :dependencies:
                dependency:micronaut-jaxrs-processor[groupId=io.micronaut.jaxrs,scope=annotationProcessor]
                dependency:micronaut-jaxrs-server[groupId=io.micronaut.jaxrs]
                :dependencies:
                
                ==== Resource
                
                :dependencies:
                dependency:micronaut-flyway[groupId=io.micronaut.flyway]
                :dependencies:
                
                Test
                
                dependency:micronaut-security-session[groupId=io.micronaut.security]
                """;

        String resJava = dependencyMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-jaxrs"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.SPOCK));
        String expectedJava = """
                ==== Dependencies
                
                When you add a `jax-rs` feature, the generated application includes the following dependencies:
                
                [source, groovy]
                .build.gradle
                ----
                annotationProcessor("io.micronaut.jaxrs:micronaut-jaxrs-processor")
                implementation("io.micronaut.jaxrs:micronaut-jaxrs-server")
                ----
                
                ==== Resource
                
                [source, groovy]
                .build.gradle
                ----
                implementation("io.micronaut.flyway:micronaut-flyway")
                ----
                
                Test
                
                [source, groovy]
                .build.gradle
                ----
                implementation("io.micronaut.security:micronaut-security-session")
                ----
                """;
        assertEquals(expectedJava, resJava);
    }
}
