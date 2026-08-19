package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class H2Gis extends AbstractFeature {
    protected H2Gis() {
        super("h2gis", "h2gis", Scope.RUNTIME);
    }
}
