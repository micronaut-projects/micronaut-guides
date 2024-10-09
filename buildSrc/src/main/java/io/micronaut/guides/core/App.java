package io.micronaut.guides.core;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.starter.application.ApplicationType;

import java.util.List;

@JsonSchema
@Serdeable
public record App (
        @NonNull
        @JsonPropertyDescription("The app's name. For single application guides, the application needs to be named default.")
        String name,

        @Nullable
        @JsonPropertyDescription("The app's package name. It you don't specify, the package name example.micronaut is used.")
        String packageName,

        @Nullable
        @JsonPropertyDescription("The app type.  It you don't specify, default is used.")
        ApplicationType applicationType,

        @Nullable
        @JsonPropertyDescription("The app's framework. Default is Micronaut but Spring Boot is also supported.")
        String framework,

        @NonNull
        @JsonPropertyDescription("The Micronaut Starter features' name that the app requires")
        List<String> features,

        @JsonPropertyDescription("The app's invisible features")
        List<String> invisibleFeatures,

        @JsonPropertyDescription("The app's Kotlin features")
        List<String> kotlinFeatures,

        @JsonPropertyDescription("The app's Java features")
        List<String> javaFeatures,

        @JsonPropertyDescription("The app's test framework")
        String testFramework,

        @JsonPropertyDescription("The tests that should not be run")
        List<String> excludeTest,

        @Nullable
        boolean validateLicense
) {
}

