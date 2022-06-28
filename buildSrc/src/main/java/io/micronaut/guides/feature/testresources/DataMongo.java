package io.micronaut.guides.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.feature.database.Data;
import jakarta.inject.Singleton;

/**
 * Add support for Micronaut Data MongoDB.

 * @author graemerocher
 */
@Singleton
public class DataMongo extends DataMongoFeature {

    private static final String SYNC_MONGODB_ARTIFACT = "mongodb-driver-sync";

    public DataMongo(Data data, TestResources testResources) {
        super(data, testResources);
    }

    @NonNull
    @Override
    public String getName() {
        return "data-mongodb-testresources";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for defining synchronous data repositories for MongoDB";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data MongoDB";
    }

    @NonNull
    @Override
    protected String mongoArtifact() {
        return SYNC_MONGODB_ARTIFACT;
    }
}
