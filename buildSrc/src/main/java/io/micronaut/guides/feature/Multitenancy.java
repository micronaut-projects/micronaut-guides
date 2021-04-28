package io.micronaut.guides.feature;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Multitenancy implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "multitenancy";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut")
                .lookupArtifactId("micronaut-multitenancy")
                .compile()
                .build());
    }
}
