package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class TestcontainersOracleFree extends AbstractFeature {

    public TestcontainersOracleFree() {
        super("testcontainers-oracle-free", "testcontainers-oracle-free");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "org.testcontainers");
    }
}
