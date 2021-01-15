package io.micronaut.guides.features;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;

public abstract class GuideFeature implements Feature {

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }
}
