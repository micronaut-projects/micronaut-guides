package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.aws.AwsV2Sdk;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class S3 implements Feature {

    private final Provider<AwsV2Sdk> awsV2Sdk;

    public S3(Provider<AwsV2Sdk> awsV2Sdk) {
        this.awsV2Sdk = awsV2Sdk;
    }

    @NonNull
    @Override
    public String getName() {
        return "s3";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        AwsV2Sdk awsV2Sdk = this.awsV2Sdk.get();
        if (awsV2Sdk.supports(featureContext.getApplicationType()) && !featureContext.isPresent(AwsV2Sdk.class)) {
            featureContext.addFeature(awsV2Sdk);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("s3")
                .compile());
    }
}
