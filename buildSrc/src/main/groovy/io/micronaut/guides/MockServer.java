package io.micronaut.guides;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class MockServer implements Feature {
    @Override
    public @NonNull String getName() {
        return "mock-server";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public @Nullable String getThirdPartyDocumentation() {
        return "https://www.mock-server.com";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder().groupId("org.testcontainers").artifactId("mockserver").test().build());
        generatorContext.addDependency(Dependency.builder().groupId("org.mock-server").artifactId("mockserver-netty").version("5.15.0").test().build());
    }
}
