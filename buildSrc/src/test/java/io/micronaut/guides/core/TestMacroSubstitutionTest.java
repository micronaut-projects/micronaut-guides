package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@MicronautTest(startApplication = false)
class TestMacroSubstitutionTest {

    @Inject
    TestMacroSubstitution testMacroSubstitution;

    @Test
    void testSubstitute() {
        String str = "test:HelloControllerTest[]";
        String resJava = testMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-http-client"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                [source,java]
                .src/test/java/example/micronaut/HelloControllerTest.java
                ----
                include::{sourceDir}/micronaut-http-client/micronaut-http-client-gradle-java/src/test/java/example/micronaut/HelloControllerTest.java[lines=16..-1]
                ----""";
        assertEquals(expectedJava, resJava);
        String resKotlin = testMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-http-client"), new GuidesOption(BuildTool.GRADLE, Language.KOTLIN, TestFramework.KOTEST));
        String expectedKotlin = """
                [source,kotlin]
                .src/test/kotlin/example/micronaut/HelloControllerTest.kt
                ----
                include::{sourceDir}/micronaut-http-client/micronaut-http-client-gradle-kotlin/src/test/kotlin/example/micronaut/HelloControllerTest.kt[lines=16..-1]
                ----""";
        assertEquals(expectedKotlin, resKotlin);
        String resGroovy = testMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-http-client"), new GuidesOption(BuildTool.GRADLE, Language.GROOVY, TestFramework.SPOCK));
        String expectedGroovy = """
                [source,groovy]
                .src/test/groovy/example/micronaut/HelloControllerSpec.groovy
                ----
                include::{sourceDir}/micronaut-http-client/micronaut-http-client-gradle-groovy/src/test/groovy/example/micronaut/HelloControllerSpec.groovy[lines=16..-1]
                ----""";
        assertEquals(expectedGroovy, resGroovy);
    }

    @Test
    void TestSubstituteWithApp() {
        String str = "test:ApplicationTest[app=springboot]";
        String resJava = testMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("spring-boot-to-micronaut-application-class"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                [source,java]
                .springboot/src/test/java/example/micronaut/ApplicationTest.java
                ----
                include::{sourceDir}/spring-boot-to-micronaut-application-class/spring-boot-to-micronaut-application-class-gradle-java/springboot/src/test/java/example/micronaut/ApplicationTest.java[lines=16..-1]
                ----""";
        assertEquals(expectedJava, resJava);
    }

    @Test
    void TestSubstituteWithTags(){
        String str = "test:TeamConfigurationTest[tags=teamConfigClassNoBuilder;gettersandsetters]";
        String resJava = testMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-configuration"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                [source,java]
                .src/test/java/example/micronaut/TeamConfigurationTest.java
                ----
                include::{sourceDir}/micronaut-configuration/micronaut-configuration-gradle-java/src/test/java/example/micronaut/TeamConfigurationTest.java[lines=16..-1,tags=teamConfigClassNoBuilder;gettersandsetters]
                ----""";
        assertEquals(expectedJava, resJava);
    }

    @Test
    void TestSubstituteWithMultiple(){
        String str = "test:TeamConfigurationTest[app=springboot,tags=teamConfigClassNoBuilder;gettersandsetters,indent=0]";
        String resJava = testMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-configuration"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                [source,java]
                .springboot/src/test/java/example/micronaut/TeamConfigurationTest.java
                ----
                include::{sourceDir}/micronaut-configuration/micronaut-configuration-gradle-java/springboot/src/test/java/example/micronaut/TeamConfigurationTest.java[lines=16..-1,tags=teamConfigClassNoBuilder;gettersandsetters,indent=0]
                ----""";
        assertEquals(expectedJava, resJava);
    }

    @Test
    void TestSubstituteMultipleLines(){
        String str = """
                Test
                
                test:TeamConfigurationTest[app=springboot,tags=teamConfigClassNoBuilder;gettersandsetters,indent=0]
                """;
        String resJava = testMacroSubstitution.substitute(str, GuideTestUtils.guideWithSlug("micronaut-configuration"), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expectedJava = """
                Test
            
                [source,java]
                .springboot/src/test/java/example/micronaut/TeamConfigurationTest.java
                ----
                include::{sourceDir}/micronaut-configuration/micronaut-configuration-gradle-java/springboot/src/test/java/example/micronaut/TeamConfigurationTest.java[lines=16..-1,tags=teamConfigClassNoBuilder;gettersandsetters,indent=0]
                ----
                """;
        assertEquals(expectedJava, resJava);
    }
}
