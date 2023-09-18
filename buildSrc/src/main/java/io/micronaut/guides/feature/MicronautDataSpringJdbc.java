package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class MicronautDataSpringJdbc implements Feature {
    @Override
    public @NonNull String getName() {
        return "micronaut-data-spring-jdbc";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.dataDependency().artifactId("micronaut-data-spring-jdbc").compile());
    }
}
