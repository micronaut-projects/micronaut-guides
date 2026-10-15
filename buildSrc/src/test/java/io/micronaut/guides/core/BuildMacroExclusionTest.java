package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.json.JsonMapper;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
public class BuildMacroExclusionTest {

    @Inject
    BuildMacroExclusion buildMacroExclusion;

    @Inject
    JsonMapper jsonMapper;

    @Inject
    ResourceLoader resourceLoader;

    @Test
    void testExclusionGradle() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        String str = """
                Run the unit test:
                
                :exclude-for-build:maven
                
                [source, bash]
                .users
                ----
                ./gradlew test
                ----
                
                :exclude-for-build:
                
                :exclude-for-build:gradle
                
                [source, bash]
                .users
                ----
                ./mvnw test
                ----
                
                :exclude-for-build:
                
                {empty} +
                
                ==== Running the application
                
                Run the `users` microservice:
                
                :exclude-for-build:maven
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./gradlew run
                ----
                
                :exclude-for-build:
                
                :exclude-for-build:gradle
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./mvnw mn:run
                ----
                
                :exclude-for-build:
                
                [source]
                ----
                14:28:34.034 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 499ms. Server Running: http://localhost:8081
                ----
                
                """;
        String result = buildMacroExclusion.substitute(str, guide, option);
        String expected = """
                Run the unit test:
                
                
                [source, bash]
                .users
                ----
                ./gradlew test
                ----
                
                
                
                {empty} +
                
                ==== Running the application
                
                Run the `users` microservice:
                
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./gradlew run
                ----
                
                
                
                [source]
                ----
                14:28:34.034 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 499ms. Server Running: http://localhost:8081
                ----
                
                """;
        assertEquals(expected, result);
    }

    @Test
    void testExclusionMaven() {
        GuidesOption option = new GuidesOption(BuildTool.MAVEN, Language.JAVA, TestFramework.JUNIT);
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        String str = """
                Run the unit test:
                
                :exclude-for-build:maven
                
                [source, bash]
                .users
                ----
                ./gradlew test
                ----
                
                :exclude-for-build:
                
                :exclude-for-build:gradle
                
                [source, bash]
                .users
                ----
                ./mvnw test
                ----
                
                :exclude-for-build:
                
                {empty} +
                
                ==== Running the application
                
                Run the `users` microservice:
                
                :exclude-for-build:maven
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./gradlew run
                ----
                
                :exclude-for-build:
                
                :exclude-for-build:gradle
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./mvnw mn:run
                ----
                
                :exclude-for-build:
                
                [source]
                ----
                14:28:34.034 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 499ms. Server Running: http://localhost:8081
                ----
                
                """;
        String result = buildMacroExclusion.substitute(str, guide, option);
        String expected = """
                Run the unit test:
                
                
                                
                [source, bash]
                .users
                ----
                ./mvnw test
                ----
                
                
                {empty} +
                
                ==== Running the application
                
                Run the `users` microservice:

                
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./mvnw mn:run
                ----
                
                
                [source]
                ----
                14:28:34.034 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 499ms. Server Running: http://localhost:8081
                ----
                
                """;
        assertEquals(expected, result);
    }
}
