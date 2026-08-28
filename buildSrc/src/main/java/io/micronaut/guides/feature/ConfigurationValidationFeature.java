package io.micronaut.guides.feature;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.ConfigurationValidationBlock;
import io.micronaut.starter.feature.validation.ConfigurationValidationProvider;
import io.micronaut.starter.feature.validation.DefaultConfigurationValidationProvider;
import jakarta.inject.Singleton;

import java.util.List;

@Singleton
@Replaces(DefaultConfigurationValidationProvider.class)
public class ConfigurationValidationFeature implements Feature, ConfigurationValidationProvider {

    @Override
    public String getName() {
        return "configuration-validation";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
    }

    @Override
    public ConfigurationValidationBlock configurationValidation(GeneratorContext generatorContext) {
        if (generatorContext.getFeatures().contains(getName())) {
            return ConfigurationValidationBlock.builder()
                    .enabled(true)
                    .failOnNotPresent(true)
                    .validateDependencyInjection(false)
                    .cacheEnabled(true)
                    .build();
        }
        if (generatorContext.getBuildTool().isGradle()) {
            return null;
        }
        return ConfigurationValidationBlock.builder()
                .enabled(false)
                .suppressions(List.of("datasources.default.db-type"))
                .failOnNotPresent(true)
                .validateDependencyInjection(false)
                .cacheEnabled(true)
                .build();
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}
