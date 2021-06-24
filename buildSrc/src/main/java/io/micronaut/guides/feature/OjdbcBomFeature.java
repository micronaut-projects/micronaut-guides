package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.COMPILE;

@Singleton
public class OjdbcBomFeature implements Feature {

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("ojdbc-bom")
                .pom(true)
                .scope(COMPILE));
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
