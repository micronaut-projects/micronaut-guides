package io.micronaut.guides.feature.springboot.replacements;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.other.HttpClient;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;
import static io.micronaut.guides.feature.springboot.SpringBootApplicationFeature.*;

@Singleton
@Replaces(HttpClient.class)
public class HttpClientReplacement extends HttpClient  {
    @Override
    public boolean shouldApply(
            ApplicationType applicationType,
            Options options,
            Set<Feature> selectedFeatures) {
        return super.shouldApply(applicationType, options, selectedFeatures) &&
                !isSpringBootApplication(selectedFeatures);
    }

}
