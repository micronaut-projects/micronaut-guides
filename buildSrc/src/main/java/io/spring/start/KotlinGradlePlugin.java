package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerWritable;
import io.spring.start.templates.kotlinCompileTask;
import jakarta.inject.Singleton;

@Singleton
public class KotlinGradlePlugin implements Feature {

    private final CoordinateResolver coordinateResolver;

    public KotlinGradlePlugin(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public @NonNull String getName() {
        return "kotlin-jvm-gradle-plugin";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            coordinateResolver.resolve("kotlin-gradle-plugin").ifPresent(coordinate ->
                    generatorContext.addBuildPlugin(GradlePlugin.builder()
                            .id("org.jetbrains.kotlin.jvm")
                            .version(coordinate.getVersion())
                            .extension(new RockerWritable(kotlinCompileTask.template()))
                            .build()));
        }
    }
}
