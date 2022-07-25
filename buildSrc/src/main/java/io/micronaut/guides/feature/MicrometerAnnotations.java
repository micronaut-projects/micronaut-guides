package io.micronaut.guides.feature;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class MicrometerAnnotations implements Feature {

    private String artifactId = "micronaut-micrometer-annotation";

    @Override
    public String getName() {
        return artifactId;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                        .groupId("io.micronaut.micrometer")
                        .artifactId(this.artifactId)
                        .versionProperty("micronaut.micrometer.version")
                        .annotationProcessor());
    }
}
