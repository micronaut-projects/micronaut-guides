package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;
import static io.micronaut.starter.build.dependencies.Scope.TEST;

@Singleton
public class Turbo extends AbstractFeature {
    public Turbo() {
        super("turbo", "micronaut-views-core");
    }
}
