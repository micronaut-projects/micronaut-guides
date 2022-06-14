package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class JakartaPersistenceApi extends AbstractFeature {

    public JakartaPersistenceApi() {
        super("jakarta-persistence-api", "jakarta.persistence-api");
    }
}
