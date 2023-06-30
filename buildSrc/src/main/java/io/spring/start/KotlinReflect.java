package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class KotlinReflect implements Feature {
    @Override
    public @NonNull String getName() {
        return "kotlin-reflect";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder().groupId("org.jetbrains.kotlin").artifactId("kotlin-reflect").scope(Scope.COMPILE).build());
    }
}
