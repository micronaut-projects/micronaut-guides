package io.spring.start;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.config.Configuration;
import io.micronaut.starter.feature.config.ConfigurationFeature;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.PropertiesTemplate;
import io.micronaut.starter.template.Template;
import jakarta.inject.Singleton;

import java.util.Set;
import java.util.function.Function;

@Singleton
public class SpringBootPropertiesConfigurationFeature implements ConfigurationFeature, DefaultFeature {

    public static final String NAME = "spring-boot-properties";
    private static final String EXTENSION = "properties";

    @Override
    public String getTargetFramework() {
        return SpringBootFramework.FRAMEWORK_SPRING_BOOT;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Java Properties Configuration";
    }

    @Override
    public String getDescription() {
        return "Creates a properties configuration file";
    }

    @Override
    public int getOrder() {
        return FeaturePhase.HIGHEST.getOrder();
    }

    @Override
    public Function<Configuration, Template> createTemplate() {
        return (config) -> new PropertiesTemplate(config.getFullPath(EXTENSION), config);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(ConfigurationFeature.class::isInstance);
    }

}
