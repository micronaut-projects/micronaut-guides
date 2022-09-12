package io.micronaut.guides.feature.springboot;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.Features;

import java.util.Set;

/**
 * Marker interface for features which can be applied only to Spring Boot Apps.
 */
public interface SpringBootApplicationFeature extends Feature {

    static boolean isSpringBootApplication(GeneratorContext generatorContext) {
        return isSpringBootApplication(generatorContext.getFeatures());
    }

    static boolean isSpringBootApplication(Features features) {
        return features.isFeaturePresent(SpringBootApplicationFeature.class);
    }

    static boolean isSpringBootApplication(Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().anyMatch(SpringBootApplicationFeature.class::isInstance);
    }
}
