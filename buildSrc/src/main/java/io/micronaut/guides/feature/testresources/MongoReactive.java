package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

@Singleton
public class MongoReactive extends MongoFeature {

    public static final String NAME = "mongo-reactive-testresources";

    protected MongoReactive(TestResources testResources) {
        super(testResources);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MongoDB Reactive Driver";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for the MongoDB Reactive Streams Driver";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.mongodb")
                .artifactId("micronaut-mongo-reactive")
                .compile());
    }
}
