package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class ReactorCore extends AbstractFeature {
    protected ReactorCore() {
        super("reactor-core");
    }
}
