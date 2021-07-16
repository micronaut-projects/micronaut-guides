package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class TestReactorCore extends AbstractFeature {
    protected TestReactorCore() {
        super("test-reactor-core", "reactor-core", Scope.TEST);
    }
}
