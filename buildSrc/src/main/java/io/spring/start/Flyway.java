package io.spring.start;

import jakarta.inject.Singleton;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;

@Singleton
public class Flyway implements Feature {

    private static final Dependency DEPENDENCY_FLYWAY_CORE = Dependency.builder()
            .groupId("org.flywaydb")
            .artifactId("flyway-core")
            .compile()
            .build();

    @Override
    public String getTitle() {
        return "Flyway Migration";
    }

    @Override
    public String getDescription() {
        return "Version control for your database so you can migrate from any version (incl. an empty database) to the latest version of the schema.";
    }

    @Override
    public @NonNull String getName() {
        return "flyway-core";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_FLYWAY_CORE);
    }
}
