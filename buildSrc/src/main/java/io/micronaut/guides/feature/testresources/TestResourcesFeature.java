package io.micronaut.guides.feature.testresources;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;

public abstract class TestResourcesFeature implements Feature  {
    private final TestResources testResources;

    protected TestResourcesFeature() {
        this.testResources = null;
    }

    protected TestResourcesFeature(TestResources testResources) {
        this.testResources = testResources;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(TestResources.class) && testResources != null) {
            featureContext.addFeature(testResources);
        }


    }
}

