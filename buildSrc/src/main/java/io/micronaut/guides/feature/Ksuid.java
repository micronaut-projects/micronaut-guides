package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class Ksuid extends AbstractFeature {

    public Ksuid() {
        super("ksuid");
    }
}
