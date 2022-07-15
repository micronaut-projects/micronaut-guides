package io.micronaut.guides.feature.testresources;

import io.micronaut.starter.application.ApplicationType;

/**
 * Base class for our mongo features.
 */
public abstract class MongoFeature extends TestResourcesFeature {

    protected MongoFeature(TestResources testResources) {
        super(testResources);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-mongodb/latest/guide/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.mongodb.com";
    }
}
