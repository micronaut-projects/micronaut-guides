package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;

import jakarta.inject.Singleton;

@Singleton
public class SecretsManager extends AbstractFeature {

    public SecretsManager() {
        super("aws-secrets-manager", "micronaut-aws-secretsmanager");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);

        addDependency(generatorContext, "micronaut-aws-sdk-v2");
    }
}
