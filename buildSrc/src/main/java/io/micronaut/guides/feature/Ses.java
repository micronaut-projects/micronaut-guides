package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class Ses extends AbstractFeature {

    public Ses() {
        super("ses");
    }
}
