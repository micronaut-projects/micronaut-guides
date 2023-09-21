package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class WiremockStandalone extends AbstractFeature {
    protected WiremockStandalone() {
        super("wiremock-standalone", "wiremock-standalone", Scope.TEST);
    }
}
