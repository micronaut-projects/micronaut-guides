package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class TestcontainersFloci extends AbstractFeature {

    public TestcontainersFloci() {
        super("testcontainers-floci", Scope.TEST);
    }
}
