package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class MacroUtilsTest {

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
        String result = MacroUtils.mainPath("appName", "fileName", option);
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
}
