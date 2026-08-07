package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class TestContainersJunitJupiter implements Feature {
    public static final Dependency DEPENDENCY_TESTCONTAINERS_JUNIT_JUPITER = Dependency.builder()
            .groupId("org.testcontainers")
            .artifactId("junit-jupiter")
            .test()
            .build();
    @Override
    public @NonNull String getName() {
        return "testcontainers-junit-jupiter";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_TESTCONTAINERS_JUNIT_JUPITER);
    }
}
