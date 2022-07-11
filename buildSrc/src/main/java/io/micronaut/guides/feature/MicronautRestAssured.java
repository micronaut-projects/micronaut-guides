package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class MicronautRestAssured implements Feature {
    private static final String ARTIFACT_ID_MICRONAUT_TEST_REST_ASSURED = "micronaut-test-rest-assured";

    @NonNull
    @Override
    public String getName() {
        return "micronaut-test-rest-assured";
    }

    @Override
    public String getTitle() {
        return "Micronaut-Test REST-assured";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "A small Micronaut-Test utility module that helps integrate the REST-assured library";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                        .lookupArtifactId("micronaut-test-rest-assured")
                .test());
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-test/latest/guide/#restAssured";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://rest-assured.io/#docs";
    }
}