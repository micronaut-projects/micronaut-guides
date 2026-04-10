package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GuideGenerationUtilsTest {
    private static final Logger LOG = LoggerFactory.getLogger(GuideGenerationUtilsTest.class);

    @Inject
    GuidesConfiguration configuration;

    @Test
    void testMainPath() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        String result = GuideGenerationUtils.mainPath("appName", "fileName", option, configuration);

        assertEquals("appName/src/main/java/example/micronaut/fileName.java", result);
    }

    @Test
    void testTestPath() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        String result = GuideGenerationUtils.testPath("appName", "fileNameTest", option, configuration);

        assertEquals("appName/src/test/java/example/micronaut/fileNameTest.java", result);
    }

    @Test
    void testPathByFolder() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        String result = GuideGenerationUtils.pathByFolder("appName", "fileName", "main", option, configuration);

        assertEquals("appName/src/main/java/example/micronaut/fileName.java", result);
    }

    @Test
    void testGuidesOptions() {
        Guide guideMetadata = new Guide(null,null, null, null, null, null, null, null,false,false,null, List.of(Language.JAVA, Language.KOTLIN),null, List.of(BuildTool.GRADLE, BuildTool.MAVEN), TestFramework.JUNIT,null,null,true,null,null,null);

        List<GuidesOption> result = GuideGenerationUtils.guidesOptions(guideMetadata, LOG);

        assertEquals(4, result.size());
    }

    @Test
    void testTestFrameworkOption() {
        assertEquals(TestFramework.SPOCK, GuideGenerationUtils.testFrameworkOption(Language.GROOVY, null));
        assertEquals(TestFramework.JUNIT, GuideGenerationUtils.testFrameworkOption(Language.JAVA, null));
        assertEquals(TestFramework.SPOCK, GuideGenerationUtils.testFrameworkOption(Language.JAVA, TestFramework.SPOCK));
    }

    @Test
    void testResolveJdkVersion() {
        JdkVersion result = GuideGenerationUtils.resolveJdkVersion(configuration);

        assertNotNull(result);
    }
}