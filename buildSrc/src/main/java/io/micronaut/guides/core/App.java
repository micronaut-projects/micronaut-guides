package io.micronaut.guides.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.application.ApplicationType;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * @param name              The app's name. For single application guides, the application needs to be named default
 * @param packageName       The app's package name. If you don't specify, the package name example.micronaut is used
 * @param applicationType   The app type.  If you don't specify, default is used
 * @param framework         The app's framework. Default is Micronaut but Spring Boot is also supported
 * @param features          The Micronaut Starter features' name that the app requires
 * @param invisibleFeatures The app's invisible features
 * @param kotlinFeatures    The app's Kotlin features
 * @param javaFeatures      The app's Java features
 * @param groovyFeatures    The app's Groovy features
 * @param jvmFeatures       The app's features shared by the JVM languages (Java, Kotlin and Groovy)
 * @param testFramework     The app's test framework
 * @param excludeTest       The tests that should not be run
 * @param excludeSource     The source files that should not be included
 * @param validateLicense   To enable Spotless code check
 * @param pythonFeatures    The app's Python features
 */
@JsonSchema
@Serdeable
public record App(
        @NonNull
        @NotBlank
        String name,

        @JsonProperty(defaultValue = "example.micronaut")
        @Nullable
        String packageName,

        @JsonProperty(defaultValue = "DEFAULT")
        @Nullable
        ApplicationType applicationType,

        @JsonProperty(defaultValue = "Micronaut")
        @Nullable
        String framework,

        @Nullable
        List<String> features,

        @Nullable
        List<String> invisibleFeatures,

        @Nullable
        List<String> kotlinFeatures,

        @Nullable
        List<String> javaFeatures,

        @Nullable
        List<String> groovyFeatures,

        @Nullable
        List<String> jvmFeatures,

        @Nullable
        TestFramework testFramework,

        @Nullable
        List<String> excludeTest,

        @Nullable
        List<String> excludeSource,

        @JsonProperty(defaultValue = StringUtils.TRUE)
        @Nullable
        Boolean validateLicense,

        @Nullable
        List<String> pythonFeatures
) {
    public App(String name,
               String packageName,
               ApplicationType applicationType,
               String framework,
               List<String> features,
               List<String> invisibleFeatures,
               List<String> kotlinFeatures,
               List<String> javaFeatures,
               List<String> groovyFeatures,
               List<String> jvmFeatures,
               TestFramework testFramework,
               List<String> excludeTest,
               List<String> excludeSource,
               Boolean validateLicense) {
        this(name, packageName, applicationType, framework, features, invisibleFeatures,
                kotlinFeatures, javaFeatures, groovyFeatures, jvmFeatures, testFramework,
                excludeTest, excludeSource, validateLicense, null);
    }
}

