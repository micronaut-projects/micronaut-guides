package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class GraalvmPolyglot extends AbstractFeature {
    protected GraalvmPolyglot() {
        super("graalvm-polyglot", "polyglot");
    }
}
