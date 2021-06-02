package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class OracleCloudAtpFeature implements Feature {

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.oraclecloud")
                .lookupArtifactId("micronaut-oraclecloud-atp")
                .runtime()
                .build());
    }

    @NonNull
    @Override
    public String getName() {
        return "micronaut-oraclecloud-atp";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
