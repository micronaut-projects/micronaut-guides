package io.micronaut.guides.feature.springboot.replacements;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.guides.feature.springboot.SpringBootApplicationFeature;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.logging.Logback;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
@Replaces(Logback.class)
public class LogbackReplacement  extends Logback {
    @Override
    public boolean shouldApply(
            ApplicationType applicationType,
            Options options,
            Set<Feature> selectedFeatures) {
        return super.shouldApply(applicationType, options, selectedFeatures) &&
                !SpringBootApplicationFeature.isSpringBootApplication(selectedFeatures);
    }
}
