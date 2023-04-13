package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class RestAssured extends AbstractFeature {
    public RestAssured() {
        super("rest-assured", "rest-assured", TEST);
    }
}
