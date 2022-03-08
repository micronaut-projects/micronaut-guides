package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.COMPILE;

@Singleton
public class JakartaPersistenceApi2 implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "jakarta-javax-persistence";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("jakarta.persistence")
                .artifactId("jakarta.persistence-api")
                .version("2.2.3")
                .scope(COMPILE));
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
