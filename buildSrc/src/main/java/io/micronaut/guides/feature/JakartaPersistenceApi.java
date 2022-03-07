package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.COMPILE_ONLY;

@Singleton
public class JakartaPersistenceApi extends AbstractFeature {

    public JakartaPersistenceApi() {
        super("jakarta-persistence-api", "jakarta.persistence-api");
    }
}
