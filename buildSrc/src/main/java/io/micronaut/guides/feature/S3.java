package io.micronaut.guides.feature;

import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.aws.AwsV2Sdk;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class S3 extends AbstractFeature {

    private final Provider<AwsV2Sdk> awsV2Sdk;

    public S3(Provider<AwsV2Sdk> awsV2Sdk) {
        super("s3");
        this.awsV2Sdk = awsV2Sdk;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        AwsV2Sdk awsV2Sdk = this.awsV2Sdk.get();
        if (awsV2Sdk.supports(featureContext.getApplicationType()) && !featureContext.isPresent(AwsV2Sdk.class)) {
            featureContext.addFeature(awsV2Sdk);
        }
    }
}
