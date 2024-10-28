package io.micronaut.guides.core;

import io.micronaut.core.beans.BeanIntrospection;
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
public class BuildDiffLinkSubstitutionTest {
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
        String resJava = buildDiffLinkSubstitution.substitute(str, new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT), guide);
        String expectedJava = "NOTE: If you have an existing Micronaut application and want to add the functionality described here, you can https://micronaut.io/launch?features=yaml&features=graalvm&features=mqtt&features=awaitility&lang=JAVA&build=GRADLE&test=JUNIT&name=cli&type=CLI&package=example.micronaut&activity=diff[view the dependency and configuration changes from the specified features, window=\"_blank\"] and apply those changes to your application.";
        assertEquals(expectedJava, resJava);
    }
}
