package io.micronaut.guides.core;

import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@MicronautTest(startApplication = false)
class MacroUtilsTest {

    @Test
    void testGetSourceDir(){
        GuidesOption option = new GuidesOption(BuildTool.GRADLE,Language.JAVA, TestFramework.JUNIT);
        String result = MacroUtils.getSourceDir("slug", option);
        assertEquals("slug-gradle-java", result);
    }

    @Test
    void testFindMacroGroups(){
        String str = """
                https://micronaut-projects.github.io/micronaut-validation/snapshot/guide/[Micronaut validation] is built on the standard framework – https://www.jcp.org/en/jsr/detail?id=380[JSR 380], also known as Bean Validation 2.0. Micronaut Validation has built-in support for validation of beans that are annotated with `jakarta.validation` annotations.
                
                To use Micronaut Validation, you need the following dependencies:
                
                :dependencies:
                
                dependency:micronaut-validation-processor[groupId=io.micronaut.validation,scope=annotationProcessor]
                dependency:micronaut-validation[groupId=io.micronaut.validation]
                
                :dependencies:
                
                Alternatively, you can use https://micronaut-projects.github.io/micronaut-hibernate-validator/latest/guide/[Micronaut Hibernate Validator], which uses https://hibernate.org/validator/[Hibernate Validator]; a reference implementation of the validation API.
                """;
        List<String> result = MacroUtils.findMacroGroups(str,"dependencies");
        List<String> expected = List.of("""
                :dependencies:
                
                dependency:micronaut-validation-processor[groupId=io.micronaut.validation,scope=annotationProcessor]
                dependency:micronaut-validation[groupId=io.micronaut.validation]
                
                :dependencies:""");
        assertEquals(expected, result);
    }

    @Test
    void testFindMacroGroupsMultiple(){
        String str = """
                https://micronaut-projects.github.io/micronaut-validation/snapshot/guide/[Micronaut validation] is built on the standard framework – https://www.jcp.org/en/jsr/detail?id=380[JSR 380], also known as Bean Validation 2.0. Micronaut Validation has built-in support for validation of beans that are annotated with `jakarta.validation` annotations.
                
                To use Micronaut Validation, you need the following dependencies:
                
                :dependencies:
                
                dependency:micronaut-validation-processor[groupId=io.micronaut.validation,scope=annotationProcessor]
                dependency:micronaut-validation[groupId=io.micronaut.validation]
                
                :dependencies:
                
                Alternatively, you can use https://micronaut-projects.github.io/micronaut-hibernate-validator/latest/guide/[Micronaut Hibernate Validator], which uses https://hibernate.org/validator/[Hibernate Validator]; a reference implementation of the validation API.
                
                :dependencies:
                
                dependency:geb-spock[groupId=org.gebish,scope=testImplementation,version=@geb-spockVersion@]
                dependency:htmlunit-driver[groupId=org.seleniumhq.selenium,scope=testImplementation,version=@htmlunit-driverVersion@]
                
                :dependencies:
                
                Test
                """;
        List<String> result = MacroUtils.findMacroGroups(str,"dependencies");
        List<String> expected = List.of("""
                :dependencies:
                
                dependency:micronaut-validation-processor[groupId=io.micronaut.validation,scope=annotationProcessor]
                dependency:micronaut-validation[groupId=io.micronaut.validation]
                
                :dependencies:""", """
                :dependencies:
                
                dependency:geb-spock[groupId=org.gebish,scope=testImplementation,version=@geb-spockVersion@]
                dependency:htmlunit-driver[groupId=org.seleniumhq.selenium,scope=testImplementation,version=@htmlunit-driverVersion@]
                
                :dependencies:""");
        assertEquals(expected, result);
    }

    @Test
    void findMacroGroupsNested(){
        String str = """
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
                
                :exclude-for-build:gradle
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./mvnw mn:run
                ----
                
                Test
                
                :exclude-for-build:maven
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./gradlew run
                ----
                
                :exclude-for-build:
                TestTest
                
                :exclude-for-build:""";
        List<String> result = MacroUtils.findMacroGroupsNested(str,"exclude-for-build");
        List<String> expected = List.of("""
                :exclude-for-build:gradle
                
                [source, bash]
                .users
                ----
                ./mvnw test
                ----
                
                :exclude-for-build:""","""
                :exclude-for-build:maven
                
                [source, bash]
                .users
                ----
                 MICRONAUT_ENVIRONMENTS=dev ./gradlew run
                ----
                
                :exclude-for-build:""", """
               :exclude-for-build:gradle
                
               [source, bash]
               .users
               ----
                MICRONAUT_ENVIRONMENTS=dev ./mvnw mn:run
               ----

               Test

               :exclude-for-build:maven

               [source, bash]
               .users
               ----
                MICRONAUT_ENVIRONMENTS=dev ./gradlew run
               ----

               :exclude-for-build:
               TestTest

               :exclude-for-build:""");
        assertEquals(expected, result);
    }
}
