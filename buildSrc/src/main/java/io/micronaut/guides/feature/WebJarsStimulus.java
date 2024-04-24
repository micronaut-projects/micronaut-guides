package io.micronaut.guides.feature;

import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class WebJarsStimulus extends AbstractFeature {
    protected WebJarsStimulus() {
        super("webjars-stimulus", "hotwired__stimulus", Scope.RUNTIME);
    }
}
