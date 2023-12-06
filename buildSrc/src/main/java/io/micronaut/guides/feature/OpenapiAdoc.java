package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class OpenapiAdoc extends AbstractFeature {
    protected OpenapiAdoc() {
        super("openapi-adoc", "micronaut-openapi-adoc", Scope.ANNOTATION_PROCESSOR);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencyWithoutLookup(generatorContext, "io.micronaut.openapi");
    }

}
