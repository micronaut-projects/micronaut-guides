package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class TestLogback extends AbstractFeature {
    public TestLogback() {
        super("test-logback", "logback-classic", Scope.TEST);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "ch.qos.logback");
    }
}
