package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class Xray extends AbstractFeature {

    public Xray() {
        super("xray", "micronaut-aws-xray");
    }
}
