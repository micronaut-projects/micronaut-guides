package io.micronaut.guides.core;

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@MicronautTest(startApplication = false)
class MacroUtilsTest {

    @Inject
    ResourceLoader resourceLoader;

    GuidesConfiguration guidesConfiguration;
    LicenseLoader licenseLoader;

    @BeforeEach
    void setUp(){
        guidesConfiguration = new GuidesConfigurationProperties();
        licenseLoader = new DefaultLicenseLoader(guidesConfiguration,resourceLoader);
    }

    @Test
    void testExtractName() {
        String line = "source:Application[app=example]";
        String macro = "source";
        String result = MacroUtils.extractName(line, macro);
        assertEquals("Application", result);
    }

    @Test
    void testExtractAppName() {
        String line = "source:Application[app=example]";
        String result = MacroUtils.extractAppName(line);
        assertEquals("example", result);
    }

    @Test
    void testExtractTags() {
        String line = "source:Application[tags=tag1|tag2]";
        List<String> result = MacroUtils.extractTags(line);
        assertEquals(List.of("tag1", "tag2"), result);
    }

    @Test
    void testMainPath() {
        GuidesOption option = new GuidesOption(BuildTool.GRADLE,Language.JAVA, TestFramework.JUNIT);
        String result = MacroUtils.mainPath(guidesConfiguration, "appName", "fileName", option);
        assertEquals("appName/src/main/java/example/micronaut/fileName.java", result);
    }

    @Test
    void testExtractIndent() {
        String line = "source:Application[indent=4]";
        String result = MacroUtils.extractIndent(line);
        assertEquals("indent=4", result);
    }

    @Test
    void testExtractTagName() {
        String line = "source:Application[tag=example]";
        String result = MacroUtils.extractTagName(line);
        assertEquals("example", result);
    }

    @Test
    void testExtractFromParametersLine() {
        String line = "source:Application[attribute=value]";
        String result = MacroUtils.extractFromParametersLine(line, "attribute");
        assertEquals("value", result);
    }

    @Test
    void testGetSourceDir(){
        GuidesOption option = new GuidesOption(BuildTool.GRADLE,Language.JAVA, TestFramework.JUNIT);
        String result = MacroUtils.getSourceDir("slug", option);
        assertEquals("slug-gradle-java", result);
    }

    @Test
    void testAddIncludesWithTags() {
        List<String> lines;
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        String slug = "exampleSlug";
        String sourcePath = "exampleSourcePath";
        String indent = "indent=4";
        List<String> tags = List.of("tag=tag1", "tag=tag2");

        lines = MacroUtils.addIncludes(option, slug, sourcePath, licenseLoader, indent, tags);

        assertEquals(6, lines.size());
        assertEquals("[source,java]", lines.get(0));
        assertEquals(".exampleSourcePath", lines.get(1));
        assertEquals("----", lines.get(2));
        assertEquals("include::{sourceDir}/exampleSlug/exampleSlug-gradle-java/exampleSourcePath[tag=tag1,indent=4]\n", lines.get(3));
        assertEquals("include::{sourceDir}/exampleSlug/exampleSlug-gradle-java/exampleSourcePath[tag=tag2,indent=4]\n", lines.get(4));
        assertEquals("----\n", lines.get(5));
    }

    @Test
    void testAddIncludesWithoutTags() {
        List<String> lines;
        GuidesOption option = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT);
        String slug = "exampleSlug";
        String sourcePath = "exampleSourcePath";
        String indent = "indent=4";
        List<String> tags = List.of();

        lines = MacroUtils.addIncludes(option, slug, sourcePath, licenseLoader, indent, tags);

        assertEquals(5, lines.size());
        assertEquals("[source,java]", lines.get(0));
        assertEquals(".exampleSourcePath", lines.get(1));
        assertEquals("----", lines.get(2));
        assertEquals("include::{sourceDir}/exampleSlug/exampleSlug-gradle-java/exampleSourcePath[lines=16..-1;indent=4]", lines.get(3));
        assertEquals("----\n", lines.get(4));
    }
}
