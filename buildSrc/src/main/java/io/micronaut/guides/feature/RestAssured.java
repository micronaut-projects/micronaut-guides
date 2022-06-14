package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class RestAssured extends AbstractFeature {
    public RestAssured() {
        super("rest-assured", "rest-assured", Scope.TEST);
    }
}
