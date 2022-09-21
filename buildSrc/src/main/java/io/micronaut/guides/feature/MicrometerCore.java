package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;

import jakarta.inject.Singleton;

@Singleton
public class MicrometerCore extends AbstractFeature {

    public MicrometerCore() {
        super("micronaut-micrometer-core");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "io.micronaut.micrometer");
    }
}
