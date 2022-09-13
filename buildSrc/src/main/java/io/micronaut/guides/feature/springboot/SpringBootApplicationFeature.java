package io.micronaut.guides.feature.springboot;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.Features;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Marker interface for features which can be applied only to Spring Boot Apps.
 */
public interface SpringBootApplicationFeature extends Feature {

    static boolean isSpringBootApplication(GeneratorContext generatorContext) {
        return isSpringBootApplication(generatorContext.getFeatures());
    }

    static boolean isSpringBootApplication(FeatureContext featureContext) {
        //TODO after 3.7.x return isSpringBootApplication(featureContext.getSelectedFeatures());
        try {
            Field f = featureContext.getClass().getDeclaredField("selectedFeatures");
            f.setAccessible(true);
            return isSpringBootApplication((Set<Feature>) f.get(featureContext));
        } catch (NoSuchFieldException e) {
            return false;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    static boolean isSpringBootApplication(Features features) {
        return features.isFeaturePresent(SpringBootApplicationFeature.class);
    }

    static boolean isSpringBootApplication(Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().anyMatch(SpringBootApplicationFeature.class::isInstance);
    }
}
