package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.distributedconfig.DistributedConfigFeature;
import jakarta.inject.Singleton;

@Singleton
public class AzureSecretsManager extends AbstractFeature implements DistributedConfigFeature {
    protected AzureSecretsManager() {
        super("azure-secrets-manager", "micronaut-azure-secret-manager");
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "io.micronaut.azure");
    }
}
