package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

@Singleton
public class JakartaPersistenceApi extends AbstractFeature {

    public JakartaPersistenceApi() {
        super("jakarta-persistence-api", "jakarta.persistence-api");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder().lookupArtifactId("jakarta.persistence-api").compile().build());
    }
}
