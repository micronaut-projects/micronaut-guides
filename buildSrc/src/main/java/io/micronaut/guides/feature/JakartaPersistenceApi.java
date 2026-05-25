package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

@Singleton
public class JakartaPersistenceApi extends AbstractFeature {
    private static final String VERSION_JAKARTA_PERSISTENCE_API = "3.2.0";

    public JakartaPersistenceApi() {
        super("jakarta-persistence-api");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("jakarta.persistence")
                .artifactId("jakarta.persistence-api")
                .version(VERSION_JAKARTA_PERSISTENCE_API)
                .compile()
                .build());
    }
}
