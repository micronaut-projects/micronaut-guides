package io.micronaut.guides.feature.springboot;

import io.micronaut.starter.feature.OneOfFeature;

public interface SpringBootStarter extends OneOfFeature, SpringBootApplicationFeature {

    default Class<?> getFeatureClass() {
        return SpringBootStarter.class;
    }
}
