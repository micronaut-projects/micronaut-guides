package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class TestcontainersKafka extends AbstractFeature {

    public TestcontainersKafka() {
        super("testcontainers-kafka", "kafka");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "org.testcontainers");
    }
}
