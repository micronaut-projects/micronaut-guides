package io.micronaut.guides.feature;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.TestContainers;
import jakarta.inject.Singleton;

@Singleton
public class Localstack extends AbstractFeature {

    private final TestContainers testContainers;

    public Localstack(TestContainers testContainers) {
        super("localstack", "localstack", Scope.TEST);
        this.testContainers = testContainers;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(TestContainers.class)) {
            featureContext.addFeature(testContainers);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        addDependency(generatorContext, "aws-java-sdk-core", Scope.TEST);
    }
}