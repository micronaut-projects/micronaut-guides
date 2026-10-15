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
public class JdkLowerThanMacroExclusionTest {

    @Inject
    JdkLowerThanMacroExclusion jdkLowerThanMacroExclusion;

    @Inject
    JsonMapper jsonMapper;

    @Inject
    ResourceLoader resourceLoader;

    @Test
    void testExclusion() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        Optional<InputStream> inputStreamOptional = resourceLoader.getResourceAsStream("classpath:metadata-min-jdk.json");
        assertTrue(inputStreamOptional.isPresent());
        InputStream inputStream = inputStreamOptional.get();
        Guide guide = assertDoesNotThrow(() -> jsonMapper.readValue(inputStream, Guide.class));
        String str = """
                Run the unit test:
                
                :exclude-for-jdk-lower-than:21
                
                [source, bash]
                .users
                ----
                ./gradlew test
                ----
                
                :exclude-for-jdk-lower-than:
              
                [source]
                ----
                14:28:34.034 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 499ms. Server Running: http://localhost:8081
                ----
                
                """;
        String result = jdkLowerThanMacroExclusion.substitute(str, guide, option);
        String expected = """
                Run the unit test:
                
              
                [source]
                ----
                14:28:34.034 [main] INFO  io.micronaut.runtime.Micronaut - Startup completed in 499ms. Server Running: http://localhost:8081
                ----
                
                """;
        assertEquals(expected, result);
    }

}
