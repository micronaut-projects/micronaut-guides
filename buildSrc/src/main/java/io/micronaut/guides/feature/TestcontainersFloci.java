package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class TestcontainersFloci extends AbstractFeature {

    public TestcontainersFloci() {
        super("testcontainers-floci", Scope.TEST);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.floci")
                .artifactId("testcontainers-floci")
                .version("2.8.0")
                .scope(Scope.TEST));
    }
}
