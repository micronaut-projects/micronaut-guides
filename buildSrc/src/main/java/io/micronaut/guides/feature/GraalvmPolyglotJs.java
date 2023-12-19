package io.micronaut.guides.feature;

import jakarta.inject.Singleton;

@Singleton
public class GraalvmPolyglotJs extends AbstractFeature {
    protected GraalvmPolyglotJs() {
        super("graalvm-polyglot-js", "js");
    }
}
