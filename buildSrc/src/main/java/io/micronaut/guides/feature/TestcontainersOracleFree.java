package io.micronaut.guides.feature;

import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.database.TestContainers;
import jakarta.inject.Singleton;

/**
 * Adds the Testcontainers Oracle Database Free module as a test dependency.
 * <p>
 * It extends the Starter {@link TestContainers} feature so that database driver features
 * do not add Micronaut Test Resources, but unlike the Starter feature it does not configure a
 * {@code jdbc:tc:} datasource URL. Guides using this feature start and configure the container themselves.
 */
@Secondary
@Singleton
public class TestcontainersOracleFree extends TestContainers {

    public static final String NAME = "testcontainers-oracle-free";

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.testcontainers")
                .artifactId("testcontainers-oracle-free")
                .test());
    }
}
