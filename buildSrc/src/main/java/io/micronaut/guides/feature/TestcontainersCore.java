package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

/**
 * Adds the Testcontainers library for tests, without the {@code testcontainers} Starter feature, which also switches
 * the data sources of tests to Testcontainers JDBC URLs instead of Micronaut Test Resources.
 */
@Singleton
public class TestcontainersCore extends AbstractFeature {

    public TestcontainersCore() {
        super("testcontainers-core", "testcontainers", Scope.TEST);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "org.testcontainers");
    }
}
