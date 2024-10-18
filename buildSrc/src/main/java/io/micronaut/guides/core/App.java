package io.micronaut.guides.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.StringUtils;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.starter.application.ApplicationType;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 *
 * @param name The app's name. For single application guides, the application needs to be named default
 * @param packageName The app's package name. If you don't specify, the package name example.micronaut is used
 * @param applicationType The app type.  If you don't specify, default is used
 * @param framework The app's framework. Default is Micronaut but Spring Boot is also supported
 * @param features The Micronaut Starter features' name that the app requires
 * @param invisibleFeatures The app's invisible features
 * @param kotlinFeatures The app's Kotlin features
 * @param javaFeatures The app's Java features
 * @param groovyFeatures The app's Groovy features
 * @param testFramework The app's test framework
 * @param excludeTest The tests that should not be run
 * @param validateLicense To enable Spotless code check
 */
@JsonSchema
@Serdeable
public record App (
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
        String testFramework,

        @Nullable
        List<String> excludeTest,

        @JsonProperty(defaultValue = StringUtils.TRUE)
        @Nullable
        Boolean validateLicense
) {
}

