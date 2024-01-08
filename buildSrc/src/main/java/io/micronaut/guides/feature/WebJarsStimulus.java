package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class WebJarsStimulus extends AbstractFeature {
    protected WebJarsStimulus() {
        super("webjars-stimulus", "hotwired__stimulus");
    }
}
