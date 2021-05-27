package io.micronaut.guides.feature;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class S3 implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "s3";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.aws")
                .artifactId("micronaut-aws-sdk-v2")
                .compile()
                .build());
        generatorContext.addDependency(Dependency.builder()
                .groupId("software.amazon.awssdk")
                .lookupArtifactId("s3")
                .compile()
                .build());
    }
}
