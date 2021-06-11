package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class SecretsManager implements Feature {

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("micronaut-aws-secretsmanager")
                .compile());
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("micronaut-aws-sdk-v2")
                .compile());
    }

    @NonNull
    @Override
    public String getName() {
        return "aws-secrets-manager";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
