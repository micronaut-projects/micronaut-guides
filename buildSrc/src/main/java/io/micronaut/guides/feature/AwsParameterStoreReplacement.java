package io.micronaut.guides.feature;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.awsparameterstore.AwsParameterStore;
import jakarta.inject.Singleton;

//TODO delete this file after this PR is merged and a Micronaut AWS version is released https://github.com/micronaut-projects/micronaut-aws/pull/2030
@Singleton
@Replaces(AwsParameterStore.class)
public class AwsParameterStoreReplacement extends AwsParameterStore {

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.addDependency(Dependency.builder()
                .groupId("software.amazon.awssdk")
                .artifactId("ssm")
                .compile());
    }
}
