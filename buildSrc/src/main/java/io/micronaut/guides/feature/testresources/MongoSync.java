package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

@Singleton
public class MongoSync extends MongoFeature {

    public static final String NAME = "mongo-sync-testresources";

    protected MongoSync(TestResources testResources) {
        super(testResources);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "MongoDB Synchronous Driver";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for the MongoDB Synchronous Driver";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.mongodb")
                .artifactId("micronaut-mongo-sync")
                .compile());
    }
}
