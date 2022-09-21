package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Scope;
import jakarta.inject.Singleton;

@Singleton
public class Localstack extends AbstractFeature {

    public Localstack() {
        super("localstack", "localstack", Scope.TEST);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        addDependency(generatorContext, "aws-java-sdk-core", Scope.TEST);
    }
}