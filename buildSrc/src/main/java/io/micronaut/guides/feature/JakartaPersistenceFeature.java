package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class JakartaPersistenceFeature extends AbstractFeature {

    public JakartaPersistenceFeature() {
        super("jakarta.persistence-api");
    }
}
