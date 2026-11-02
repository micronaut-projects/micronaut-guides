package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class RestAssured extends AbstractFeature {
    public RestAssured() {
        super("rest-assured", "rest-assured");
    }
    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "io.rest-assured");
    }
}
