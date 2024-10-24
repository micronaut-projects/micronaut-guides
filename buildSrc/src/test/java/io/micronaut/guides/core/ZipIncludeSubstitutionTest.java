package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class ZipIncludeSubstitutionTest {

    @Inject
    ZipIncludeMacroSubstitution zipIncludeMacroSubstitution;

    @Test
    void testSubstitute() {
        String str = """
                A fast way to start using Kafka is https://hub.docker.com/r/confluentinc/cp-kafka/[via Docker]. Create this `docker-compose.yml` file:
                
                zipInclude:docker/docker-compose.yml[]""";
        String resJava = zipIncludeMacroSubstitution.substitute(str, "micronaut-kafka", new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                    A fast way to start using Kafka is https://hub.docker.com/r/confluentinc/cp-kafka/[via Docker]. Create this `docker-compose.yml` file:
                    
                    [source,yaml]
                    .docker/docker-compose.yml
                    ----
                    include::{sourceDir}/micronaut-kafka/micronaut-kafka-gradle-java/docker/docker-compose.yml[]
                    ----""";
        assertEquals(expectedJava, resJava);
    }
}