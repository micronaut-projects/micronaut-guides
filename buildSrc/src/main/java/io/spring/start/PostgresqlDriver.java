package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class PostgresqlDriver implements Feature {

    public static final Dependency DEPENDENCY_POSTGRESQL = Dependency.builder()
            .groupId("org.postgresql")
            .artifactId("postgresql")
            .runtime()
            .build();

    @Override
    public @NonNull String getName() {
        return "postgresql-driver";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getDescription() {
        return "A JDBC and R2DBC driver that allows Java programs to connect to a PostgreSQL database using standard, database independent Java code.";
    }

    @Override
    public String getTitle() {
        return "PostgreSQL Driver";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_POSTGRESQL);
    }
}
