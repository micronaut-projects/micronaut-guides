package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class TestcontainersOracleXe extends AbstractFeature {

    public TestcontainersOracleXe() {
        super("testcontainers-oracle-xe", "oracle-xe");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "org.testcontainers");
    }
}
