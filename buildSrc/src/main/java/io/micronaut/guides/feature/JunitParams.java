package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class JunitParams extends AbstractFeature {
    public JunitParams() {
        super("junit-params", "junit-jupiter-params", TEST);
    }
}
