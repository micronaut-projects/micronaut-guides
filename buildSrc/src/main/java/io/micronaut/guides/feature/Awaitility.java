package io.micronaut.guides.feature;

import javax.inject.Singleton;

import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class Awaitility extends AbstractFeature {

    public Awaitility() {
        super("awaitility", TEST);
    }
}
