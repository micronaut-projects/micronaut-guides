package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class AwsEvents extends AbstractFeature {

    public AwsEvents() {
        super("aws-events", "aws-lambda-java-events");
    }
}
