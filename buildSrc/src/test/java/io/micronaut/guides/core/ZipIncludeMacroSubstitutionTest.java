package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class ZipIncludeMacroSubstitutionTest {

    @Inject
    ZipIncludeMacroSubstitution zipIncludeMacroSubstitution;

    @Test
    void testSubstitute() {
        String str = """
                A fast way to start using Kafka is https://hub.docker.com/r/confluentinc/cp-kafka/[via Docker]. Create this `docker-compose.yml` file:
                
                zipInclude:docker/docker-compose.yml[]""";
        String resJava = zipIncludeMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-kafka"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                    A fast way to start using Kafka is https://hub.docker.com/r/confluentinc/cp-kafka/[via Docker]. Create this `docker-compose.yml` file:
                    
                    [source,yaml]
                    .docker/docker-compose.yml
                    ----
                    include::{sourceDir}/micronaut-kafka/micronaut-kafka-gradle-java/docker/docker-compose.yml[]
                    ----""";
        assertEquals(expectedJava, resJava);
    }

    @Test
    void testSubstituteWithPath() {
        String str = "zipInclude:../ttfr.sh[]";
        String resJava = zipIncludeMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("executable-jar"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                [source,sh]
                .ttfr.sh
                ----
                include::{sourceDir}/executable-jar/executable-jar-gradle-java/../ttfr.sh[]
                ----""";
        assertEquals(expectedJava, resJava);
    }
}