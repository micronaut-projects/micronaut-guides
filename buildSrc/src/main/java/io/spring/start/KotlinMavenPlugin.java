package io.spring.start;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.maven.MavenPlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.template.RockerWritable;
import io.spring.start.templates.kotlinMavenPlugin;
import jakarta.inject.Singleton;

@Singleton
public class KotlinMavenPlugin implements Feature {
    private final CoordinateResolver coordinateResolver;

    public KotlinMavenPlugin(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public @NonNull String getName() {
        return "kotlin-maven-plugin";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        coordinateResolver.resolve("kotlin-gradle-plugin").ifPresent(coordinate -> {
                    BuildProperties props = generatorContext.getBuildProperties();
                    props.put("kotlin.version", coordinate.getVersion());
                });

        generatorContext.addBuildPlugin(MavenPlugin.builder()
                .groupId("org.jetbrains.kotlin")
                .artifactId("kotlin-maven-plugin")
                .extension(new RockerWritable(kotlinMavenPlugin.template()))
                .build());
    }
}
