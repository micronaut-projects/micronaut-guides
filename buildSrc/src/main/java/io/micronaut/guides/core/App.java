package io.micronaut.guides.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.jsonschema.JsonSchema;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.options.BuildTool;

import java.util.ArrayList;
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
 * @param testFramework The app's test framework
 * @param excludeTest The tests that should not be run
 * @param validateLicense To enable Spotless code check
 */
@JsonSchema
@Serdeable
public record App (
        @NonNull
        String name,

        @Nullable
        String packageName,

        @Nullable
        ApplicationType applicationType,

        @Nullable
        String framework,

        @NonNull
        List<String> features,

        List<String> invisibleFeatures,

        List<String> kotlinFeatures,

        List<String> javaFeatures,

        String testFramework,

        List<String> excludeTest,

        @Nullable
        boolean validateLicense
) {
        @JsonCreator
        public App(String name,String packageName,ApplicationType applicationType,String framework,List<String> features, List<String> invisibleFeatures, List<String> kotlinFeatures, List<String> javaFeatures, String testFramework, List<String> excludeTest, boolean validateLicense){
                this.name=name;
                this.packageName=packageName == null ? "example.micronaut" : packageName;
                this.applicationType=applicationType == null ? ApplicationType.DEFAULT : applicationType;
                this.framework=framework == null ? "Micronaut" : framework;
                this.features=features == null ? new ArrayList<>() : features;
                this.invisibleFeatures=invisibleFeatures == null ? new ArrayList<>() : invisibleFeatures;
                this.kotlinFeatures=kotlinFeatures == null ? new ArrayList<>() : kotlinFeatures;
                this.javaFeatures=javaFeatures == null ? new ArrayList<>() : javaFeatures;
                this.testFramework=testFramework;
                this.excludeTest=excludeTest;
                this.validateLicense=validateLicense;
        }
}

