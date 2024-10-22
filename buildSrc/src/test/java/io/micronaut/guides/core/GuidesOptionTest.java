package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuidesOptionTest {

    private GuidesOption guidesOption;

    @BeforeEach
    void setUp() {
        guidesOption = new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.SPOCK);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(BuildTool.GRADLE, guidesOption.getBuildTool());
        assertEquals(Language.JAVA, guidesOption.getLanguage());
        assertEquals(TestFramework.SPOCK, guidesOption.getTestFramework());

        guidesOption.setBuildTool(BuildTool.MAVEN);
        guidesOption.setLanguage(Language.KOTLIN);
        guidesOption.setTestFramework(TestFramework.JUNIT);

        assertEquals(BuildTool.MAVEN, guidesOption.getBuildTool());
        assertEquals(Language.KOTLIN, guidesOption.getLanguage());
        assertEquals(TestFramework.JUNIT, guidesOption.getTestFramework());
    }
}