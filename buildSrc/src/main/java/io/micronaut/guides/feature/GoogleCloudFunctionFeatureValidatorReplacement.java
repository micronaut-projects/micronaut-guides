package io.micronaut.guides.feature;

import jakarta.inject.Singleton;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.gcp.AbstractGoogleCloudFunction;
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunction;
import io.micronaut.starter.feature.function.gcp.GoogleCloudFunctionFeatureValidator;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Options;

import java.util.Set;

@Replaces(GoogleCloudFunctionFeatureValidator.class)
@Singleton
public class GoogleCloudFunctionFeatureValidatorReplacement extends GoogleCloudFunctionFeatureValidator {
    private static boolean supports(JdkVersion jdkVersion) {
        return JdkVersion.JDK_11.equals(jdkVersion) ||
                JdkVersion.JDK_17.equals(jdkVersion) ||
                JdkVersion.JDK_21.equals(jdkVersion) ||
                JdkVersion.JDK_25.equals(jdkVersion);
    }

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(AbstractGoogleCloudFunction.class::isInstance)) {
            if (features.stream().anyMatch(GraalVM.class::isInstance)) {
                throw new IllegalArgumentException("""
                        Google Cloud Function is not supported for GraalVM. \
                        Consider Google Cloud Run for deploying GraalVM native images as docker containers.\
                        """);
            }
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(GoogleCloudFunction.class::isInstance) && !supports(options.getJavaVersion())) {
            throw new IllegalArgumentException("""
                    Google Cloud Function currently only supports JDK 11 and 17 -- \
                    https://cloud.google.com/functions/docs/concepts/java-runtime""");
        }
    }

}
