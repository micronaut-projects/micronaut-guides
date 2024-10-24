package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


@MicronautTest(startApplication = false)
class SourceMacroSubstitutionTest {

    @Inject
    SourceMacroSubstitution sourceMacroSubstitution;

    @Test
    void testSubstitute(){
        String str = "source:GithubConfiguration[]";
        String resJava = sourceMacroSubstitution.substitute(str, "micronaut-http-client", new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.SPOCK));
        String expectedJava = """
                [source,java]
                .src/main/java/example/micronaut/GithubConfiguration.java
                ----
                include::{sourceDir}/micronaut-http-client/micronaut-http-client-gradle-java/src/main/java/example/micronaut/GithubConfiguration.java[lines=16..-1]
                ----
                """;
        assertEquals(expectedJava, resJava);
        String resKotlin = sourceMacroSubstitution.substitute(str, "micronaut-http-client", new GuidesOption(BuildTool.GRADLE, Language.KOTLIN, TestFramework.KOTEST));
        String expectedKotlin = """
                [source,kotlin]
                .src/main/kotlin/example/micronaut/GithubConfiguration.kt
                ----
                include::{sourceDir}/micronaut-http-client/micronaut-http-client-gradle-kotlin/src/main/kotlin/example/micronaut/GithubConfiguration.kt[lines=16..-1]
                ----
                """;
        assertEquals(expectedKotlin, resKotlin);
        String resGroovy = sourceMacroSubstitution.substitute(str, "micronaut-http-client", new GuidesOption(BuildTool.GRADLE, Language.GROOVY, TestFramework.JUNIT));
        String expectedGroovy = """
                [source,groovy]
                .src/main/groovy/example/micronaut/GithubConfiguration.groovy
                ----
                include::{sourceDir}/micronaut-http-client/micronaut-http-client-gradle-groovy/src/main/groovy/example/micronaut/GithubConfiguration.groovy[lines=16..-1]
                ----
                """;
        assertEquals(expectedGroovy, resGroovy);
    }

    @Test
    void TestSubstituteWithApp(){
        String str = "source:Application[app=springboot]";
        String resJava = sourceMacroSubstitution.substitute(str, "spring-boot-to-micronaut-application-class", new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.SPOCK));
        String expectedJava = """
                [source,java]
                .springboot/src/main/java/example/micronaut/Application.java
                ----
                include::{sourceDir}/spring-boot-to-micronaut-application-class/spring-boot-to-micronaut-application-class-gradle-java/springboot/src/main/java/example/micronaut/Application.java[lines=16..-1]
                ----
                """;
        assertEquals(expectedJava, resJava);
    }

    @Test
    void TestSubstituteWithTags(){
        String str = "source:TeamConfiguration[tags=teamConfigClassNoBuilder|gettersandsetters]";
        String resJava = sourceMacroSubstitution.substitute(str, "micronaut-configuration", new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.SPOCK));
        String expectedJava = """
                [source,java]
                .src/main/java/example/micronaut/TeamConfiguration.java
                ----
                include::{sourceDir}/micronaut-configuration/micronaut-configuration-gradle-java/src/main/java/example/micronaut/TeamConfiguration.java[tag=teamConfigClassNoBuilder]
                
                include::{sourceDir}/micronaut-configuration/micronaut-configuration-gradle-java/src/main/java/example/micronaut/TeamConfiguration.java[tag=gettersandsetters]
                
                ----
                """;
        assertEquals(expectedJava, resJava);
    }

    @Test
    void TestSubstituteWithMultiple(){
        String str = "source:TeamConfiguration[app=springboot,tags=teamConfigClassNoBuilder|gettersandsetters,indent=0]";
        String resJava = sourceMacroSubstitution.substitute(str, "micronaut-configuration", new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.SPOCK));
        String expectedJava = """
                [source,java]
                .springboot/src/main/java/example/micronaut/TeamConfiguration.java
                ----
                include::{sourceDir}/micronaut-configuration/micronaut-configuration-gradle-java/springboot/src/main/java/example/micronaut/TeamConfiguration.java[tag=teamConfigClassNoBuilder,indent=0]
                
                include::{sourceDir}/micronaut-configuration/micronaut-configuration-gradle-java/springboot/src/main/java/example/micronaut/TeamConfiguration.java[tag=gettersandsetters,indent=0]
                
                ----
                """;
        assertEquals(expectedJava, resJava);
    }
}
