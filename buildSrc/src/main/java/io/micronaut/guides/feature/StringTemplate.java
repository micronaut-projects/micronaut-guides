package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class StringTemplate extends AbstractFeature {
    protected StringTemplate() {
        super("ST4", "ST4", Scope.TEST);
    }
}
