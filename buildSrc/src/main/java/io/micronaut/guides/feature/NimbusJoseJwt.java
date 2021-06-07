package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;
import javax.inject.Singleton;

@Singleton
public class NimbusJoseJwt implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "nimbus-jose-jwt";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("nimbus-jose-jwt")
                .compile()
                .build());
    }
}
