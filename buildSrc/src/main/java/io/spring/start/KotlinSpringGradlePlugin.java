package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class KotlinSpringGradlePlugin implements Feature {

    private final CoordinateResolver coordinateResolver;

    public KotlinSpringGradlePlugin(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public @NonNull String getName() {
        return "kotlin-spring-gradle-plugin";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            coordinateResolver.resolve("kotlin-gradle-plugin").ifPresent(coordinate ->
                    generatorContext.addBuildPlugin(GradlePlugin.builder().id("org.jetbrains.kotlin.plugin.spring").version(coordinate.getVersion()).build()));
        }
    }
}
