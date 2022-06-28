package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.feature.database.Data;
import jakarta.inject.Singleton;

/**
 * Add support for Micronaut Data MongoDB Reactive.
 */
@Singleton
public class DataMongoReactive extends DataMongoFeature {

    private static final String ASYNC_MONGODB_ARTIFACT = "mongodb-driver-reactivestreams";

    public DataMongoReactive(Data data, TestResources testResources) {
        super(data, testResources);
    }

    @NonNull
    @Override
    public String getName() {
        return "data-mongodb-async-testresources";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for defining asynchronous data repositories for MongoDB";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data MongoDB Async";
    }

    @NonNull
    @Override
    protected String mongoArtifact() {
        return ASYNC_MONGODB_ARTIFACT;
    }
}
