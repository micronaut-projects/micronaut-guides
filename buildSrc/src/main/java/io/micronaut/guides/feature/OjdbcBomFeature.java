package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Feature;

import jakarta.inject.Singleton;

@Singleton
public class OjdbcBomFeature implements Feature {

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("com.oracle.database.jdbc")
                .lookupArtifactId("ojdbc-bom")
                .pom(true)
                .scope(Scope.COMPILE)
                .build());
    }

    @NonNull
    @Override
    public String getName() {
        return "ojdbc-bom";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
