package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class JsonPath extends AbstractFeature {
    public JsonPath() {
        super("json-path", TEST);
    }
}
