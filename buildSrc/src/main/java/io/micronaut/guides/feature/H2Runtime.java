package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class H2Runtime extends AbstractFeature {
    protected H2Runtime() {
        super("h2-runtime", "h2", Scope.RUNTIME);
    }
}
